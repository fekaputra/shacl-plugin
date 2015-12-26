package at.ac.tuwien.shacl.plugin.events;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;

import java.io.InputStream;
import java.net.URI;
import java.util.Observable;
import java.util.UUID;

/**
 *
 */
public class ShaclValidation extends Observable {
//    public void runValidation(Dataset dataset, URI uri) throws InterruptedException {
//        System.out.println("running validation...");
//        //Model resultsModel = model;
//        Model model = ModelConstraintValidator.get().validateModel(dataset, uri, null, false, null);
//        System.out.println("validation finished...");
//        this.setChanged();
//        this.notifyObservers(model);
//        System.out.println("notifiers notified...");
//    }

    //TODO replae test data with real-time evaluation
    public void runValidation(Dataset dataset, URI uri) throws InterruptedException {
        // Load the main data model
        Model dataModel = JenaUtil.createMemoryModel();
        dataModel.read(getClass().getResourceAsStream("/example2.ttl"), "urn:dummy", FileUtils.langTurtle);

        Model dataModel2 = JenaUtil.createMemoryModel();
        dataModel2.read(getClass().getResourceAsStream("/wine.ttl"), FileUtils.langXML);

        // Load the shapes Model (here, includes the dataModel because that has templates in it)
        Model shaclModel = JenaUtil.createDefaultModel();
        InputStream is = getClass().getResourceAsStream("/shacl.ttl");
        shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
        MultiUnion unionGraph = new MultiUnion(new Graph[] {
                shaclModel.getGraph(),
                dataModel.getGraph(),
                dataModel2.getGraph()
        });
        Model shapesModel = ModelFactory.createModelForGraph(unionGraph);

        // Note that we don't perform validation of the shape definitions themselves.
        // To do that, activate the following line to make sure that all required triples are present:
        // dataModel = SHACLUtil.withDefaultValueTypeInferences(shapesModel);

        // Make sure all sh:Functions are registered
        SHACLFunctions.registerFunctions(shapesModel);

        // Create Dataset that contains both the main query model and the shapes model
        // (here, using a temporary URI for the shapes graph)
        URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
        Dataset dataset1 = ARQFactory.get().getDataset(dataModel);
        dataset1.addNamedModel(shapesGraphURI.toString(), shapesModel);

        Model results = ModelConstraintValidator.get().validateModel(dataset1, shapesGraphURI, null, false, null);

        System.out.println("validation finished...");
        this.setChanged();
        this.notifyObservers(results);
        System.out.println("notifiers notified...");
    }
}

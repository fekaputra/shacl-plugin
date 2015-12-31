package at.ac.tuwien.shacl.plugin.events;

import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.spin.arq.ARQFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    //depending on whether the shapes model and data model are separate models or not.
    public void runValidation(Model shapesModel, Model dataModel) throws InterruptedException, FileNotFoundException {
//        // Load the main data model
//        Model shapesModel = JenaUtil.createMemoryModel();
//        shapesModel.read(getClass().getResourceAsStream("/example2.ttl"), "urn:dummy", FileUtils.langTurtle);
//
//        Model dataModel = JenaUtil.createMemoryModel();
//        dataModel.read(getClass().getResourceAsStream("/wine.rdf"), FileUtils.langXML);

        dataModel.add(shapesModel);

        MultiUnion unionGraph = new MultiUnion(new Graph[] {
                ShaclModelFactory.getShaclModel().getGraph(),
                dataModel.getGraph(),
                //shapesModel.getGraph()
        });
        Model model = ModelFactory.createModelForGraph(unionGraph);

        model.write(new FileOutputStream(new File("ouput.ttl")), "TURTLE");

        // Note that we don't perform validation of the shape definitions themselves.
        // To do that, activate the following line to make sure that all required triples are present:
        // dataModel = SHACLUtil.withDefaultValueTypeInferences(shapesModel);

        // Make sure all sh:Functions are registered
        SHACLFunctions.registerFunctions(model);

        // Create Dataset that contains both the main query model and the shapes model
        // (here, using a temporary URI for the shapes graph)
        URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
        Dataset dataset = ARQFactory.get().getDataset(dataModel);
        dataset.addNamedModel(shapesGraphURI.toString(), model);

        Model results = ModelConstraintValidator.get().validateModel(dataset, shapesGraphURI, null, false, null);

        //add prefixes to results, so that views can display qualified names instead of URIs for better usability
        System.out.println("++++++++++++++++++");
        results.setNsPrefixes(model.getNsPrefixMap());

        System.out.println("++++++++++++++++++");

        results.write(System.out, "TURTLE");

//        System.out.println(model.getNsPrefixMap());
//        System.out.println("results prefixes: "+results.getNsPrefixMap());
//        System.out.println("base: "+model.getNsPrefixURI(""));
//        System.out.println("results base: "+model.getNsPrefixURI(""));
//
//        System.out.println("validation finished...");
        this.setChanged();
        this.notifyObservers(results);
    }
}

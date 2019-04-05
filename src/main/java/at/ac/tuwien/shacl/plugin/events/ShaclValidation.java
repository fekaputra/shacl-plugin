package at.ac.tuwien.shacl.plugin.events;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Observable;
import java.util.UUID;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.jenax.util.ARQFactory;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.jenax.util.SystemTriples;

/**
 *
 */
public class ShaclValidation extends Observable {

    public void runValidation2(Model shaclModel, Model dataModel) throws InterruptedException, FileNotFoundException {

        // Load the shapes Model (here, includes the dataModel because that has shape definitions too)
        // MultiUnion unionGraph = new MultiUnion(new Graph[] { shaclModel.getGraph(), dataModel.getGraph() });
        // Model shapesModel = ModelFactory.createModelForGraph(unionGraph);

        // Make sure all sh:Functions are registered
        Model completeShaclModel = getSHACLModel();
        completeShaclModel.add(shaclModel);
        SHACLFunctions.registerFunctions(completeShaclModel);

        // Create Dataset that contains both the main query model and the shapes model
        // (here, using a temporary URI for the shapes graph)
        URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
        Dataset dataset = ARQFactory.get().getDataset(dataModel);
        dataset.addNamedModel(shapesGraphURI.toString(), completeShaclModel);

        // Run the validator
//        Model results = ValidationUtil.validateModel(dataset, shapesGraphURI, true);
        Resource results = ValidationUtil.validateModel(dataModel, completeShaclModel, true);

        // print stuff
        System.out.println("--- ************* ---");
        shaclModel.write(new FileOutputStream("shacl.ttl"), "TURTLE");
        System.out.println("--- ************* ---");
        dataModel.write(new FileOutputStream("data.ttl"), "TURTLE");

        // Print violations
        System.out.println("--- ************* ---");
        System.out.println(ModelPrinter.get().print(results.getModel()));

        this.setChanged();
        this.notifyObservers(results);
    }

    public static Model getSHACLModel() {
        Model shaclModel = null;
        if (shaclModel == null) {
            InputStream shaclTTL = ShaclValidation.class.getResourceAsStream("/etc/shacl.ttl");
            InputStream dashTTL = ShaclValidation.class.getResourceAsStream("/etc/dash.ttl");

            shaclModel = JenaUtil.createDefaultModel();
            shaclModel.read(shaclTTL, SH.BASE_URI, FileUtils.langTurtle);
            shaclModel.read(dashTTL, SH.BASE_URI, FileUtils.langTurtle);
            shaclModel.add(SystemTriples.getVocabularyModel());

            SHACLFunctions.registerFunctions(shaclModel);
        }
        return shaclModel;
    }
}

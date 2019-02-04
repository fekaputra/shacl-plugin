package at.ac.tuwien.shacl.plugin;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.jenax.util.ARQFactory;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.jenax.util.SystemTriples;

public class ValidationExample {

    /**
     * Loads an example SHACL file and validates all constraints. This file can also be used as a starting point for
     * your own custom applications.
     */
    public static void main(String[] args) throws Exception {

        // Load the shapes Model (here, includes the dataModel because that has shape definitions too)
        // MultiUnion unionGraph = new MultiUnion(new Graph[] { shaclModel.getGraph(), dataModel.getGraph() });
        Model ruleModel = ModelFactory.createDefaultModel();
        Model dataModel = ModelFactory.createDefaultModel();
        ruleModel.read("src/test/resources/shacl.ttl", "TURTLE");
        dataModel.read("src/test/resources/data.ttl", "TURTLE");

        // Make sure all sh:Functions are registered

        ruleModel.add(getSHACLModel());
        SHACLFunctions.registerFunctions(ruleModel);

        // Create Dataset that contains both the main query model and the shapes model
        // (here, using a temporary URI for the shapes graph)
        URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
        Dataset dataset = ARQFactory.get().getDataset(dataModel);
        dataset.addNamedModel(shapesGraphURI.toString(), ruleModel);

        // Run the validator
        Resource results = ValidationUtil.validateModel(dataModel, ruleModel, true);

        // print stuff
        System.out.println("--- ************* ---");
        dataModel.write(System.out, "TURTLE");
        System.out.println("--- ************* ---");
        ruleModel.write(System.out, "TURTLE");

        // Print violations
        System.out.println("--- ************* ---");
//        System.out.println(ModelPrinter.get().print(results));
        System.out.println(results);
    }

    public static Model getSHACLModel() {
        Model shaclModel = null;
        if (shaclModel == null) {
            InputStream shaclTTL = ValidationExample.class.getResourceAsStream("/etc/shacl.ttl");
            InputStream dashTTL = ValidationExample.class.getResourceAsStream("/etc/dash.ttl");

            shaclModel = JenaUtil.createDefaultModel();
            shaclModel.read(shaclTTL, SH.BASE_URI, FileUtils.langTurtle);
            shaclModel.read(dashTTL, SH.BASE_URI, FileUtils.langTurtle);
            shaclModel.add(SystemTriples.getVocabularyModel());

            SHACLFunctions.registerFunctions(shaclModel);
        }
        return shaclModel;
    }
}

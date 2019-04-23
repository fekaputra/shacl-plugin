package at.ac.tuwien.shacl.plugin;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.validation.ValidationUtil;

public class ValidationExample {

    /**
     * Loads an example SHACL file and validates all constraints. This file can also be used as a starting point for
     * your own custom applications.
     */
    public static void main(String[] args) throws Exception {

        Model ruleModel = ModelFactory.createDefaultModel();
        Model dataModel = ModelFactory.createDefaultModel();
        ruleModel.read("src/main/resources/example3.ttl", "TURTLE");
        dataModel.read("src/main/resources/example3-data.ttl", "TURTLE");

        // Run the validator
        Resource results = ValidationUtil.validateModel(dataModel, ruleModel, true);

        // print stuff
        System.out.println("--- ************* ---");
        dataModel.write(System.out, "TURTLE");
        System.out.println("--- ************* ---");
        ruleModel.write(System.out, "TURTLE");

        // Print violations
        System.out.println("--- ************* ---");
        System.out.println(ModelPrinter.get().print(results.getModel()));
    }
}

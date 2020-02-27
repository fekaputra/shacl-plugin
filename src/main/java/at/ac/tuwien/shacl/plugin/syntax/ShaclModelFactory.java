package at.ac.tuwien.shacl.plugin.syntax;

import at.ac.tuwien.shacl.plugin.util.RdfModelReader;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;

public class ShaclModelFactory {

    public static Model getBaseModel() {
        Model model = ModelFactory.createDefaultModel();

        model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
        model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
        model.setNsPrefix("ex", "http://www.example.org/#");

        return model;
    }

    public static Model getExampleModel() throws IOException {
        return RdfModelReader.getModelFromFile("/example3.ttl");
    }

    public static String getExampleModelAsString() throws IOException {
        return RdfModelReader.getModelFromFileAsString("/example3.ttl");
    }
}

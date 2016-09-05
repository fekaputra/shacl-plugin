package at.ac.tuwien.shacl.plugin.syntax;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import at.ac.tuwien.shacl.plugin.util.RdfModelReader;

public class ShaclModelFactory {
    private static Model shaclModel = RdfModelReader.getModelFromFile("/shacl.ttl");

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

    public static Model getExampleModel() {
        return RdfModelReader.getModelFromFile("/example2.ttl");
    }

    public static Model getShaclModel() {
        return shaclModel;
    }

    public static String getExampleModelAsString() {
        return RdfModelReader.getModelFromFileAsString("/example2.ttl");
    }
}

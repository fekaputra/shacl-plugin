package at.ac.tuwien.shacl.plugin.util;

import java.io.*;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;

/**
 * Reads RDF models.
 */
public class RdfModelReader {
    /**
     * Get a Jena model from a Turtle file.
     *
     * @param path path to the Turtle file
     * @return a Jena model containing RDF data
     */
    public static Model getModelFromFile(String path) throws IOException {
        return getModelFromFile(path, "TURTLE");
    }

    /**
     * Get a Jena model from an RDF file in an RDF serialization language.
     *
     * @param path path to the RDF file
     * @param language a string containing an RDF serialization language; see the Jena model.read method for a list of
     *        supported languages
     * @return Jena model containing the RDF data
     */
    public static Model getModelFromFile(String path, String language) throws IOException {
        try (InputStream in = ShaclModelFactory.class.getResourceAsStream(path)) {
            Model model = ModelFactory.createDefaultModel();
            model.read(in, "", language);
            return model;
        }
    }

    /**
     * Get a Jena model from an RDF string in an RDF serialization language.
     * Processes owl:imports statements.
     *
     * @param text the RDF string
     * @param language a string containing an RDF serialization language; see the Jena model.read method for a list of
     *        supported languages
     * @return Jena model containing the RDF data and imported statements
     */
    public static Model getModelFromString(String text, String language) {
        Model shapesModel = ModelFactory.createOntologyModel();
        shapesModel.read(new ByteArrayInputStream(text.getBytes()), null, language);
        return shapesModel;
    }

    /**
     * Get a model as string by reading it line by line. This is an alternative to Jena model.read, allowing comments to
     * be kept intact.
     *
     * @return an RDF model as a string
     */
    public static String getModelFromFileAsString(String path) throws IOException {
        try (InputStream in = ShaclModelFactory.class.getResourceAsStream(path);
             InputStreamReader is = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(is);
        ) {
            String newLine = System.getProperty("line.separator");
            return br.lines().collect(Collectors.joining(newLine));
        }
    }
}

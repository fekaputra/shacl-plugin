package at.ac.tuwien.shacl.plugin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;

/**
 * Reads RDF models from files.
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
            StringBuilder sb = new StringBuilder();
            String read = br.readLine();
            String newLine = System.getProperty("line.separator");

            while (read != null) {
                sb.append(read);
                sb.append(newLine);
                read = br.readLine();
            }

            return sb.toString();
        }
    }
}

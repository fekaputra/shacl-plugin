package at.ac.tuwien.shacl.plugin.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import at.ac.tuwien.shacl.plugin.events.ShaclValidation;

/**
 * Provides convenience methods for test execution.
 */
public class TestUtil {

    private static final URL example3Url     = ShaclValidation.class.getClassLoader().getResource("example3.ttl");
    private static final URL example3DataUrl = ShaclValidation.class.getClassLoader().getResource("example3-data.ttl");

    public static Model getDataModel() throws IOException {
        try (InputStream in = example3DataUrl.openStream()) {
            Model dataModel = ModelFactory.createDefaultModel();
            dataModel.read(in, "urn:dummy", FileUtils.langTurtle);

            return dataModel;
        }
    }

    public static Model getShapesModel() throws IOException {
        try (InputStream in = example3Url.openStream()) {
            Model shapesModel = ModelFactory.createDefaultModel();
            shapesModel.read(in, "urn:dummy", FileUtils.langTurtle);

            return shapesModel;
        }
    }

}

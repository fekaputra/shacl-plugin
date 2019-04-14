package at.ac.tuwien.shacl.plugin.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileUtils;

import org.topbraid.jenax.util.JenaUtil;

import at.ac.tuwien.shacl.plugin.events.ShaclValidation;

/**
 * Provides convenience methods for test execution.
 */
public class TestUtil {

    public static Model getDataModel() {
        Model dataModel = JenaUtil.createDefaultModel();
        dataModel.read(ShaclValidation.class.getClassLoader().getResourceAsStream("example3-data.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        return dataModel;
    }

    public static Model getShapesModel() {
        Model shapesModel = JenaUtil.createDefaultModel();
        shapesModel.read(ShaclValidation.class.getClassLoader().getResourceAsStream("example3.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        return shapesModel;
    }

}

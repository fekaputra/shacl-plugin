package at.ac.tuwien.shacl.plugin;

import java.io.FileNotFoundException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;

import org.apache.jena.vocabulary.RDF;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.jenax.util.JenaUtil;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import at.ac.tuwien.shacl.plugin.events.ShaclValidation;


/**
 * Tests the Shacl engine.
 */
public class TestShaclValidation {
    @Test
    public void testWineConstraint() throws FileNotFoundException, InterruptedException {
        Model dataModel = JenaUtil.createDefaultModel();
        dataModel.read(getClass().getResourceAsStream("/wine/wineShape.ttl"), "", FileUtils.langTurtle);

        Model shapesModel = JenaUtil.createDefaultModel();
        shapesModel.read(getClass().getResourceAsStream("/wine/wine.rdf"), "", FileUtils.langXML);

        // Run the validator and print results
        Resource results =
                ValidationUtil.validateModel(dataModel, shapesModel, false);

        results.getModel().write(System.out, "TURTLE");

        // TODO: test anything? Should it conform or not?
    }

    @Test
    public void testExample3() throws InterruptedException {

        // Load the main data model
        Model dataModel = JenaUtil.createDefaultModel();
        dataModel.read(ShaclValidation.class.getClassLoader().getResourceAsStream("example3-data.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        Model shapesModel = JenaUtil.createDefaultModel();
        shapesModel.read(ShaclValidation.class.getClassLoader().getResourceAsStream("example3.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        // Run the validator and print results
        Resource results =
                ValidationUtil.validateModel(dataModel, shapesModel, false);

        results.getModel().write(System.out, "TURTLE");

        // Expecting 3 constraint violations
        assertEquals(3, results.getModel().listStatements(null, RDF.type, SH.ValidationResult).toList().size());
    }
}

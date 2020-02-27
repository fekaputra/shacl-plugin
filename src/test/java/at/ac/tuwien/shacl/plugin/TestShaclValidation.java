package at.ac.tuwien.shacl.plugin;

import at.ac.tuwien.shacl.plugin.util.ShaclValidationReport;
import at.ac.tuwien.shacl.plugin.util.ShaclValidationResult;
import at.ac.tuwien.shacl.plugin.util.TestUtil;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.junit.Test;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.validation.ValidationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests the Shacl engine.
 */
public class TestShaclValidation {
    @Test public void testWineConstraint() throws IOException {
        Model dataModel = JenaUtil.createDefaultModel();
        Model shapesModel = JenaUtil.createDefaultModel();

        try (InputStream dataIn = getClass().getResourceAsStream("/wine/wine.rdf");
                InputStream shapesIn = getClass().getResourceAsStream("/wine/wineShape.ttl");) {
            dataModel.read(dataIn, "", FileUtils.langXML);
            shapesModel.read(shapesIn, "", FileUtils.langTurtle);
        }

        // Run the validator and print results
        Resource results = ValidationUtil.validateModel(dataModel, shapesModel, false);

        results.getModel().write(System.out, "TURTLE");

        // TODO: test anything? Should it conform or not?
    }

    @Test public void testExample3() throws IOException {

        // Load the main data model
        Model dataModel = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        // Run the validator and print results
        Resource results = ValidationUtil.validateModel(dataModel, shapesModel, false);

        results.getModel().write(System.out, "TURTLE");

        ShaclValidationReport report = new ShaclValidationReport(results);

        assertFalse("Model should not conform", report.conforms);
        assertEquals("There should be three violations", 3, report.validationResults.size());

        Set<String> violationMessages = new HashSet<>(3);
        for (ShaclValidationResult res : report.validationResults) {
            violationMessages.add(res.resultMessage.toString());
        }

        Set<String> expectedMessages = new HashSet<>(
                Arrays.asList("Predicate ex:birthDate is not allowed (closed shape)",
                        "Value does not match pattern \\\"^\\d{3}-\\d{2}-\\d{4}$\\\"",
                        "Property may only have 1 value, but found 2"));

        assertEquals("Expected violation report Messages", expectedMessages, violationMessages);
    }
}

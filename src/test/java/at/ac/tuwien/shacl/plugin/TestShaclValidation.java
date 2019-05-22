package at.ac.tuwien.shacl.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;

import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.validation.ValidationUtil;

import org.junit.Test;

import at.ac.tuwien.shacl.plugin.util.ShaclValidationReport;
import at.ac.tuwien.shacl.plugin.util.ShaclValidationResult;
import at.ac.tuwien.shacl.plugin.util.TestUtil;

import static org.junit.Assert.*;

/**
 * Tests the Shacl engine.
 */
public class TestShaclValidation {
    @Test
    public void testWineConstraint() throws IOException {
        Model dataModel = JenaUtil.createDefaultModel();
        Model shapesModel = JenaUtil.createDefaultModel();

        try (InputStream dataIn = getClass().getResourceAsStream("/wine/wine.rdf");
             InputStream shapesIn = getClass().getResourceAsStream("/wine/wineShape.ttl");
        ) {
            dataModel.read(dataIn, "", FileUtils.langXML);
            shapesModel.read(shapesIn, "", FileUtils.langTurtle);
        }


        // Run the validator and print results
        Resource results =
                ValidationUtil.validateModel(dataModel, shapesModel, false);

        results.getModel().write(System.out, "TURTLE");

        // TODO: test anything? Should it conform or not?
    }

    @Test
    public void testExample3() throws IOException {

        // Load the main data model
        Model dataModel   = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        // Run the validator and print results
        Resource results =
                ValidationUtil.validateModel(dataModel, shapesModel, false);

        results.getModel().write(System.out, "TURTLE");

        ShaclValidationReport report = new ShaclValidationReport(results);

        assertFalse("Model should not conform", report.conforms);
        assertEquals("There should be three violations", 3, report.validationResults.size());

        Set<String> violationMessages = new HashSet<>(3);
        for (ShaclValidationResult res : report.validationResults) {
            violationMessages.add(res.resultMessage.toString());
        }

        Set<String> expectedMessages = new HashSet<>(Arrays.asList("More than 1^^http://www.w3.org/2001/XMLSchema#integer values", "Predicate http://www.example.org/#birthDate is not allowed (closed shape)", "Value does not match pattern \\\"^\\d{3}-\\d{2}-\\d{4}$\\\""));

        assertEquals("Expected violation report Messages", expectedMessages, violationMessages);
    }
}

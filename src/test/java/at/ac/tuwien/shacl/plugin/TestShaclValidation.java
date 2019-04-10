package at.ac.tuwien.shacl.plugin;

import java.io.IOException;
import java.io.InputStream;

import at.ac.tuwien.shacl.plugin.util.TestUtil;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;

import org.apache.jena.vocabulary.RDF;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.jenax.util.JenaUtil;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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

        // Expecting 3 constraint violations
        assertEquals(3, results.getModel().listStatements(null, RDF.type, SH.ValidationResult).toList().size());
    }
}

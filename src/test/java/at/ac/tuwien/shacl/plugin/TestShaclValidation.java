package at.ac.tuwien.shacl.plugin;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.UUID;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.compose.MultiUnion;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;
import org.junit.Test;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;

import at.ac.tuwien.shacl.plugin.events.ShaclValidation;
import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;

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

        ShaclValidation validation = new ShaclValidation();
        validation.runValidation2(shapesModel, dataModel);
    }

    @Test
    public void testSHACLSquare() throws InterruptedException {

        // Load the main data model
        Model dataModel = JenaUtil.createDefaultModel();
        dataModel.read(getClass().getResourceAsStream("/topbraid/shaclsquare.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        MultiUnion unionGraph =
            new MultiUnion(new Graph[] { ShaclModelFactory.getShaclModel().getGraph(), dataModel.getGraph() });
        Model shapesModel = ModelFactory.createModelForGraph(unionGraph);

        // Note that we don't perform validation of the shape definitions themselves.
        // To do that, activate the following line to make sure that all required triples are present:
        // dataModel = SHACLUtil.withDefaultValueTypeInferences(shapesModel);

        // Make sure all sh:Functions are registered
        SHACLFunctions.registerFunctions(shapesModel);

        // Create Dataset that contains both the main query model and the shapes model
        // (here, using a temporary URI for the shapes graph)
        URI shapesGraphURI = URI.create("urn:x-shacl-shapes-graph:" + UUID.randomUUID().toString());
        Dataset dataset = ARQFactory.get().getDataset(dataModel);
        dataset.addNamedModel(shapesGraphURI.toString(), shapesModel);

        // Run the validator and print results
        Model results =
            ModelConstraintValidator.get().validateModel(dataset, shapesGraphURI, null, false, null, null);
        // System.out.println(ModelPrinter.get().print(results));
        results.write(System.out, "TURTLE");
        // Expecting 2 constraint violations (9 triples each)
        // assertEquals(18, results.size());
    }
}

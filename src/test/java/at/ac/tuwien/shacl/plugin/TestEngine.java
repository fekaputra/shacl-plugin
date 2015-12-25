package at.ac.tuwien.shacl.plugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import org.junit.Test;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;

public class TestEngine {
	
	@Test
	public void testEngine2() throws InterruptedException {
		// Load the main data model
		Model dataModel = JenaUtil.createMemoryModel();
		dataModel.read(getClass().getResourceAsStream("/example2.ttl"), "urn:dummy", FileUtils.langTurtle);
		
		Model dataModel2 = JenaUtil.createMemoryModel();
		dataModel2.read(getClass().getResourceAsStream("/wine.ttl"), FileUtils.langXML);
		
		
		// Load the shapes Model (here, includes the dataModel because that has templates in it)
		Model shaclModel = JenaUtil.createDefaultModel();
		InputStream is = getClass().getResourceAsStream("/shacl.ttl");
		shaclModel.read(is, SH.BASE_URI, FileUtils.langTurtle);
		MultiUnion unionGraph = new MultiUnion(new Graph[] {
			shaclModel.getGraph(),
			dataModel.getGraph(),
			dataModel2.getGraph()
		});
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
		Model results = ModelConstraintValidator.get().validateModel(dataset, shapesGraphURI, null, false, null);
	
		results.write(System.out, FileUtils.langTurtle);
	}
//	}
}

package at.ac.tuwien.shacl.plugin.syntax;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class SHACLModelFactory {
	public static Model getBaseModel() {
		Model model=ModelFactory.createDefaultModel();
		model.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
		
		return model;
	}
	
	public static Model addToBaseModel(Model model) throws Exception {
		if(!model.getNsPrefixURI("sh").equals("http://www.w3.org/ns/shacl#") || model.getNsPrefixURI("sh")!=null) {
			throw new Exception();
		}
		
		return model;
	}
}

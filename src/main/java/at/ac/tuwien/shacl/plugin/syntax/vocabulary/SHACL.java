package at.ac.tuwien.shacl.plugin.syntax.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;


/**
 * Define shacl keywords here. Corresponding Resource (jena)?
 * @author xlin
 *
 */
public class SHACL {
	public final static String BASE_URI = "http://www.w3.org/ns/shacl";
	
	public final static String NS = BASE_URI + "#";
	
	public final static String PREFIX = "sh";
	
	public final static Resource SHAPE  = ResourceFactory.createResource(NS + "shape");

	public final static Resource PROPERTY  = ResourceFactory.createResource(NS + "property");
	
	public final static Property PREDICATE = ResourceFactory.createProperty(NS + "predicate");
}

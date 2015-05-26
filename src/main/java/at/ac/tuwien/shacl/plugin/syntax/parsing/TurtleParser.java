package at.ac.tuwien.shacl.plugin.syntax.parsing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.jena.riot.RiotException;

import uk.ac.manchester.cs.owl.owlapi.turtle.parser.ParseException;
import at.ac.tuwien.shacl.plugin.syntax.SHACLModelFactory;
import at.ac.tuwien.shacl.plugin.syntax.vocabulary.SHACL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TurtleParser implements Parser {

	@Override
	public void parse() throws ParsingException {
		try {
			Model model=SHACLModelFactory.getBaseModel();
			//model.createResource(is.toString());
			//model.setNsPrefix("ex", "http://example.org/demo#");
			
			model.read("query1.ttl", "TURTLE");
			model.write(System.out, "RDF/XML-ABBREV");
			model.write(System.out, "TURTLE");
			
			if(model.contains(SHACL.PROPERTY, SHACL.PREDICATE)) {
				System.out.println("shacl contains "+SHACL.PROPERTY+" with property"+SHACL.PREDICATE);
			}
		} catch(RiotException e) {
			throw new ParsingException(e.getMessage());
		}
		
		
//		try {
//			InputStream is = new FileInputStream("query1.ttl");
//			com.hp.hpl.jena.n3.turtle.parser.TurtleParser parser = new com.hp.hpl.jena.n3.turtle.parser.TurtleParser(is);
//			parser.parse();
//		} catch (FileNotFoundException | com.hp.hpl.jena.n3.turtle.parser.ParseException e) {
//			e.printStackTrace();
//		}
//		try {
//			InputStream is = new FileInputStream("query1.ttl");
//			uk.ac.manchester.cs.owl.owlapi.turtle.parser.TurtleParser parser = new uk.ac.manchester.cs.owl.owlapi.turtle.parser.TurtleParser(is);
//			parser.parseDocument();;
//		} catch (FileNotFoundException | ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		/*try {
			InputStream is = new FileInputStream("query1.ttl");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

}

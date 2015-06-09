package at.ac.tuwien.shacl.plugin.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class ShaclModelFactory {
	public static Model getBaseModel() {
		Model model=ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		model.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
		model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
		model.setNsPrefix("ex", "http://www.example.org/#");
		return model;
	}

	public static Model getExampleModel() {
    	InputStream in = ShaclModelFactory.class.getResourceAsStream("/example1.ttl");
    	Model model = ModelFactory.createDefaultModel();
    	model.read(in, "", "TURTLE");
    	return model;
	}
	
	public static String getExampleModelAsString() {
		StringBuilder sb = null;
		
		try {
			InputStream in = ShaclModelFactory.class.getResourceAsStream("/example1.ttl");
			InputStreamReader is = new InputStreamReader(in);
			sb=new StringBuilder();
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();
			String newLine = System.getProperty("line.separator");
			
			while(read != null) {
			    sb.append(read);
			    sb.append(newLine);
			    read =br.readLine();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}

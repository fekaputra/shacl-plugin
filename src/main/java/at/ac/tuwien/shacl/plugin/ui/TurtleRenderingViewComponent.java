package at.ac.tuwien.shacl.plugin.ui;

import java.io.ByteArrayOutputStream;
import java.io.Writer;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.protege.editor.owl.ui.view.ontology.AbstractOntologyRenderingViewComponent;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;

import com.hp.hpl.jena.rdf.model.Model;

public class TurtleRenderingViewComponent extends AbstractOntologyRenderingViewComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 4981062527906093700L;

    @Override
    protected void renderOntology(OWLOntology ontology, Writer writer) throws Exception {
        //OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        JenaOwlConverter converter = new JenaOwlConverter();
    	
        Model ontologyModel = converter.ModelOwlToJenaConvert2(ontology, "TURTLE");
        ontologyModel.write(out, "TURTLE");
        
    	//manager.saveOntology(getOWLModelManager().getActiveOntology(), new TurtleOntologyFormat(), out);
    	writer.write(out.toString());
    }
}

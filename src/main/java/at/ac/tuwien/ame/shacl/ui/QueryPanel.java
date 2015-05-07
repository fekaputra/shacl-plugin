package at.ac.tuwien.ame.shacl.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Logger;
import org.coode.owlapi.rdf.model.RDFNode;
import org.coode.owlapi.rdf.model.RDFTranslator;
import org.coode.owlapi.rdf.model.RDFTriple;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.protege.editor.owl.ProtegeOWL;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;

public class QueryPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    private JButton execButton = new JButton("Execute");
    private JTextArea editorTextArea = new JTextArea("I'm a text. Please scroll me.");
    private OWLModelManager modelManager;
    private static final Logger log = Logger.getLogger(QueryPanel.class);

    private ActionListener refreshAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // recalculate();
        }
    };

    private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
        public void handleChange(OWLModelManagerChangeEvent event) {
            if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
                // recalculate();
            }
        }
    };

    public QueryPanel(OWLModelManager modelManager) {
        setLayout(new BorderLayout());

        log.info("in editor panel");
        this.modelManager = modelManager;
        // recalculate();
        log.info("model manager set");
        modelManager.addListener(modelListener);
        // refreshButton.addActionListener(refreshAction);
        log.info("listener added");
        JScrollPane scroll = new JScrollPane(editorTextArea);

        add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

        execButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	//validate(editorTextArea.getText());
                execute2();
            }
        });

        log.info("text component added");

    }

    private void execute2() {
        OWLOntology ont = modelManager.getActiveOntology();

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
			manager.saveOntology(ont, new TurtleOntologyFormat(), new FileOutputStream(new File("ontology.ttl")));
		
			System.out.println("saved");
	        Model model = ModelFactory.createOntologyModel();
	        model.read("ontology.ttl");
	        editorTextArea.setText(model.getGraph().toString());
        } catch (OWLOntologyStorageException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void execute() {
        StringBuffer sb = new StringBuffer();
        OWLOntology ont = modelManager.getActiveOntology();
        Collection<OWLAxiom> axioms = ont.getTBoxAxioms(false);
        Iterator<OWLAxiom> axiomsIt = axioms.iterator();
        while (axiomsIt.hasNext()) {
            OWLAxiom axiom = axiomsIt.next();
            sb.append("\n");
            sb.append(axiom);
        }
        if (sb.toString().isEmpty()) {
            editorTextArea.setText("Empty Ontology");
        } else {
            editorTextArea.setText(sb.toString());
        }
        
        //FIXME throws NoClassDefFound error
//    	try {
//    		Model model=SHACLModelFactory.getBaseModel();
//        	model.read(editorTextArea.getText(), "TURTLE");
//    	} catch(RiotException e) {
//    		editorTextArea.setText(e + "\n\n" + editorTextArea.getText());
//    	}
    }
    
    private void validate(String query) {
    	
    	InputStream spinIS = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
    	Model model = ModelFactory.createDefaultModel();
        model.read(spinIS, null);
        RDFDataMgr.write(System.out, model, Lang.TURTLE);
        
//		SHACLValidator validator = new SHACLValidator(model);
//		RDFDataMgr.write(System.out, validator.validateGraph(), Lang.TURTLE);
    }

    /*private void transformSpinToSparql(String spinString) {

        // spin initialization
         SPINModuleRegistry.get().init();

         //convert string into byteArray for initialization
         InputStream spinIS = new ByteArrayInputStream(spinString.getBytes(StandardCharsets.UTF_8));

         //jena model initialization
         Model model = ModelFactory.createDefaultModel();
         model.read(spinIS, null);

         //write the result out
         RDFDataMgr.write(System.out, model, Lang.TURTLE);

    }*/

    public void dispose() {
        modelManager.removeListener(modelListener);
        // refreshButton.removeActionListener(refreshAction);
    }
}

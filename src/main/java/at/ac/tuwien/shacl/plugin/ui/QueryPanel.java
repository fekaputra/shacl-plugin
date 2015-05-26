package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import at.ac.tuwien.shacl.plugin.syntax.JenaToOwlConvert;
import at.ac.tuwien.shacl.plugin.syntax.SHACLModelFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class QueryPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    private JButton execButton = new JButton("Execute");
    private JTextPane editorTextPane = new JTextPane();
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
        JScrollPane scroll = new JScrollPane(editorTextPane);

        add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

        editorTextPane.setFont(new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize()));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SHACLModelFactory.getBaseModel().write(out, "TURTLE");
        editorTextPane.setText(out.toString() + "\n###### add constraint definition######\n");
        
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
			manager.saveOntology(ont, new TurtleOntologyFormat());
			
			System.out.println("saved");
	        Model model = ModelFactory.createDefaultModel();
	        model.read("ontology.ttl");
	        editorTextPane.setText(model.getGraph().toString());
        } catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void execute3() {
    	OWLOntology ont = modelManager.getActiveOntology();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        
        JenaToOwlConvert converter = new JenaToOwlConvert();
        
        OntModel jenamodel = converter.ModelOwlToJenaConvert(ont, "TURTLE");
        Model normalJenamodel = converter.ModelOwlToJenaConvert2(ont, "TURTLE");
        try {
			jenamodel.write(new FileOutputStream(new File("jena_ontology.ttl")), "TURTLE");
			normalJenamodel.write(new FileOutputStream(new File("jena_normal.ttl")), "TURTLE");
        } catch (FileNotFoundException e) {
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
            editorTextPane.setText("Empty Ontology");
        } else {
            editorTextPane.setText(sb.toString());
        }
        
        //FIXME throws NoClassDefFound error
//    	try {
//    		Model model=SHACLModelFactory.getBaseModel();
//        	model.read(editorTextArea.getText(), "TURTLE");
//    	} catch(RiotException e) {
//    		editorTextArea.setText(e + "\n\n" + editorTextArea.getText());
//    	}
    }
    
//    private void validate(String query) {
//    	
//    	InputStream shacl = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
//    	Model model = ModelFactory.createDefaultModel();
//        model.read(shacl, null);
//        SHACLValidator validator = new SHACLValidator(model);
//        Model errorModel = validator.validateGraph();
//        editorTextArea.setText(errorModel.getGraph().toString());
//        errorModel.write(System.out, "TURTLE");
////		SHACLValidator validator = new SHACLValidator(model);
////		RDFDataMgr.write(System.out, validator.validateGraph(), Lang.TURTLE);
//    }

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

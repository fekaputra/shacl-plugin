package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.model.OWLOntology;

import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;
import at.ac.tuwien.shacl.plugin.syntax.SHACLModelFactory;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class QueryPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    private JButton execButton = new JButton("Execute");
    private JTextPane editorPane = new JTextPane();
    private OWLModelManager modelManager;
    private JTextPane errorPane = new JTextPane();
    private static final Logger log = Logger.getLogger(QueryPanel.class);

    private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
        public void handleChange(OWLModelManagerChangeEvent event) {
            if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
                // recalculate();
            }
        }
    };
    
    private ActionListener execButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
            execute();
        }
    };
    
    public QueryPanel(OWLModelManager modelManager) {
        this.init(modelManager);
    }
    
    private void init(OWLModelManager modelManager) {
    	setLayout(new BorderLayout());
        
        Font font = new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize());

        //set model manager
        this.modelManager = modelManager;
        modelManager.addListener(modelListener);
        
        //add text editor related functionality
        JScrollPane scroll = new JScrollPane(editorPane);
        add(scroll, BorderLayout.CENTER);

        editorPane.setFont(font);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SHACLModelFactory.getBaseModel().write(out, "TURTLE");
        editorPane.setText(out.toString() + "\n###### add constraint definitions ######\n");

        //add "Execute" button related functionality
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);
        execButton.addActionListener(this.execButtonAction);
        
        //add error log related functionality
        JScrollPane scrollError = new JScrollPane(errorPane);
        buttonPanel.add(scrollError, BorderLayout.CENTER);
        buttonPanel.setPreferredSize(new Dimension(100,100));
        
        errorPane.setFont(font);
        errorPane.setBackground(null);
        errorPane.setEditable(false);
        errorPane.setText("this is the error log");
        
        
    }

    private void execute() {
    	OWLOntology ont = modelManager.getActiveOntology();

    	JenaOwlConverter converter = new JenaOwlConverter();
    	
        Model ontologyModel = converter.ModelOwlToJenaConvert2(ont, "TURTLE");
        Model constraintModel = ModelFactory.createDefaultModel();

    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		ontologyModel.write(out, "TURTLE");

        constraintModel.read(new ByteArrayInputStream(editorPane.getText().getBytes()), "", "TURTLE");
        constraintModel.add(ontologyModel);

		SHACLValidator validator = new SHACLValidator(constraintModel);
		out = new ByteArrayOutputStream();
		Model errorModel = validator.validateGraph();

		errorModel.write(out, "TURTLE");
		errorPane.setText(out.toString());
		
		log.info("done");
    }
    
//    private void execute() {
//        StringBuffer sb = new StringBuffer();
//        OWLOntology ont = modelManager.getActiveOntology();
//        Collection<OWLAxiom> axioms = ont.getTBoxAxioms(false);
//        Iterator<OWLAxiom> axiomsIt = axioms.iterator();
//        while (axiomsIt.hasNext()) {
//            OWLAxiom axiom = axiomsIt.next();
//            sb.append("\n");
//            sb.append(axiom);
//        }
//        if (sb.toString().isEmpty()) {
//            editorPane.setText("Empty Ontology");
//        } else {
//            editorPane.setText(sb.toString());
//        }
//    }
//
    public void dispose() {
        modelManager.removeListener(modelListener);
    }
}

package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.jena.riot.RiotException;
import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLOntology;

import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;
import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;
import at.ac.tuwien.shacl.plugin.ui.util.ShaclCallbackNotifier;
import at.ac.tuwien.shacl.util.SHACLParsingException;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ShaclEditorPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    
    private JButton execButton = new JButton("Validate");
    private JTextPane editorPane = new JTextPane();
    
    private static final Logger log = Logger.getLogger(ShaclEditorPanel.class);
    
    private OWLModelManager modelManager;
    
    private SHACLValidator validator;
    private Thread thread;

    private ActionListener execButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
            validateGraph();
        }
    };
    
//    private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
//        public void handleChange(OWLModelManagerChangeEvent event) {
//            if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
//                // recalculate();
//            }
//        }
//    };
    
    public ShaclEditorPanel(OWLModelManager modelManager) {
        this.init(modelManager);
    }
    
    private void init(OWLModelManager modelManager) {
    	//init shacl validator in its own thread, because it takes a while
    	this.thread = new Thread(new SHACLValidatorInitializer());
    	thread.start();
    	
    	setLayout(new BorderLayout());

        Font font = new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize());

        //set model manager
        this.modelManager = modelManager;

        //add text editor related functionality
        JScrollPane scroll = new JScrollPane(editorPane);
        add(scroll, BorderLayout.CENTER);

        editorPane.setFont(font);
        editorPane.setText(ShaclModelFactory.getExampleModelAsString() + "\n###### add SHACL vocabulary ######\n");

        //add "Execute" button related functionality
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        execButton.addActionListener(this.execButtonAction);
    }
    
    private void validateGraph() {
    	try {
    		//wait for validator thread to finish, so the validator is not null
            try {
    			thread.join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		
	    	JenaOwlConverter converter = new JenaOwlConverter();
	
			OWLOntology ont = modelManager.getActiveOntology();
	        Model ontologyModel = converter.ModelOwlToJenaConvert2(ont, "TURTLE");
	
	        Model constraintModel = ModelFactory.createDefaultModel();
	        constraintModel.read(new ByteArrayInputStream(editorPane.getText().getBytes()), "", "TURTLE");
	        constraintModel.add(ontologyModel);

			Model errorModel = validator.validateGraph(constraintModel);

			String message;
			System.out.println("validation finished");
			
			if(errorModel.isEmpty()) {
				message = "no constraint violations";
			} else {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				errorModel.write(out, "TURTLE");
				message = out.toString();
			}
			
			ShaclCallbackNotifier.notify(message);
    	} catch(RiotException | SHACLParsingException e) {
    		ShaclCallbackNotifier.notify(e.getLocalizedMessage());
    		System.out.println(e.getLocalizedMessage());
    		//ErrorDialog ed = new ErrorDialog();
    		//ed.showDialog(e.getLocalizedMessage());
    	} catch(QueryException e) {
    		ShaclCallbackNotifier.notify("Encountered query error: "+e.getLocalizedMessage());
    	}
    }
    
    public void dispose() {
    }
    
    private class SHACLValidatorInitializer implements Runnable {
		@Override
		public void run() {
			SHACLValidator validator = SHACLValidator.getValidator();
			ShaclEditorPanel.this.validator = validator;
		}
    }
}

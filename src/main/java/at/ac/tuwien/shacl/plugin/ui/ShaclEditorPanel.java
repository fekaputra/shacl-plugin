package at.ac.tuwien.shacl.plugin.ui;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;
import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;
import org.apache.jena.riot.RiotException;
import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.topbraid.shacl.arq.SHACLFunctions;
import org.topbraid.shacl.constraints.ModelConstraintValidator;
import org.topbraid.shacl.vocabulary.SH;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.util.JenaUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.UUID;

public class ShaclEditorPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    
    private JButton execButton = new JButton("Validate");
    private JTextPane editorPane = new JTextPane();

    private OWLModelManager modelManager;

    private Thread thread;

    private ActionListener execButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
//			if(validator == null) {
//				//wait for validator thread to finish, so the validator is not null when used
//	            try {
//	    			thread.join();
//	    			
//	    			thread = null;
//	    		} catch (InterruptedException exc) {
//	    			exc.printStackTrace();
//	    		}
//			}
//			
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
    	//this.thread = new Thread(new SHACLValidatorInitializer());
    	//thread.start();
    	
    	this.setLayout(new BorderLayout());

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
			JenaOwlConverter converter = new JenaOwlConverter();

			OWLOntology ont = modelManager.getActiveOntology();
	        Model dataModel = converter.ModelOwlToJenaConvert2(ont, "TURTLE");

            // Load the main data model
            Model shapesModel = JenaUtil.createDefaultModel();
            shapesModel.read(new ByteArrayInputStream(editorPane.getText().getBytes()), "urn:dummy", FileUtils.langTurtle);

			ShaclValidationRegistry.getValidator().runValidation(dataModel, shapesModel);
        } catch(RiotException e) {
			ErrorNotifier.notify(e.getLocalizedMessage());
    	} catch(QueryException e) {
			ErrorNotifier.notify("Encountered query error: "+e.getLocalizedMessage());
    	} catch(Exception e) {
            e.printStackTrace();
            ErrorNotifier.notify("Something went wrong. Please check the error log for more information.");
    	}
    }
    
    public void dispose() {
    }
    
//    private class SHACLValidatorInitializer implements Runnable {
//		@Override
//		public void run() {
//			ModelConstraintValidator.get();
////			SHACLValidator validator = SHACLValidator.getDefaultValidator();
////			ShaclEditorPanel.this.validator = validator;
//		}
//    }
}

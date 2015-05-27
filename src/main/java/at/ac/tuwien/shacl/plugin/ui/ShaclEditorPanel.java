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
import at.ac.tuwien.shacl.plugin.ui.util.ShaclCallbackNotifier;
import at.ac.tuwien.shacl.validation.SHACLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ShaclEditorPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    private JButton execButton = new JButton("Validate");
    private JTextPane editorPane = new JTextPane();
    private static final Logger log = Logger.getLogger(QueryPanel.class);

    private ActionListener execButtonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
            execute();
        }
    };
    
    public ShaclEditorPanel(OWLModelManager modelManager) {
        this.init(modelManager);
    }
    
    private void init(OWLModelManager modelManager) {
    	setLayout(new BorderLayout());
        
        Font font = new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize());

        //add text editor related functionality
        JScrollPane scroll = new JScrollPane(editorPane);
        add(scroll, BorderLayout.CENTER);

        editorPane.setFont(font);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SHACLModelFactory.getBaseModel().write(out, "TURTLE");
        editorPane.setText(out.toString() + "\n###### add SHACL vocabulary ######\n");

        //add "Execute" button related functionality
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        execButton.addActionListener(this.execButtonAction);
    }
    
    private void execute() {
    	JenaOwlConverter converter = new JenaOwlConverter();
    	
    	Model constraintModel = ModelFactory.createDefaultModel();
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	constraintModel.read(new ByteArrayInputStream(editorPane.getText().getBytes()), "", "TURTLE");
        
    	SHACLValidator validator = new SHACLValidator(constraintModel);
		out = new ByteArrayOutputStream();
		Model errorModel = validator.validateGraph();
		
		String message;
		
		if(errorModel.isEmpty()) {
			message = "no constraint violations";
		} else {
			errorModel.write(out, "TURTLE");
			message = out.toString();
		}
		
		ShaclCallbackNotifier.notify(message);
    }
    
    public void dispose() {
    }
}

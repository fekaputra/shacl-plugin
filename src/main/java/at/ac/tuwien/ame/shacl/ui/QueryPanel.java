package at.ac.tuwien.ame.shacl.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

public class QueryPanel extends JPanel {
	private static final long serialVersionUID = -2739474730975140803L;
	private JButton execButton = new JButton("Execute");
	private JTextArea editorTextArea = new JTextArea("I'm a text. Please scroll me.");
	private OWLModelManager modelManager;
	private static final Logger log = Logger.getLogger(QueryPanel.class);
	
	private ActionListener refreshAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            //recalculate();
        }
    };
    
    private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
		public void handleChange(OWLModelManagerChangeEvent event) {
			if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
				//recalculate();
			}
		}
    };
    
    public QueryPanel(OWLModelManager modelManager) {
    	setLayout(new BorderLayout());
    	
    	log.info("in editor panel");
    	this.modelManager = modelManager;
        //recalculate();
        log.info("model manager set");
        modelManager.addListener(modelListener);
        //refreshButton.addActionListener(refreshAction);
        log.info("listener added");
        JScrollPane scroll = new JScrollPane(editorTextArea); 
        add(scroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

        log.info("text component added");
        
    }
    
    public void dispose() {
        modelManager.removeListener(modelListener);
        //refreshButton.removeActionListener(refreshAction);
    }
}

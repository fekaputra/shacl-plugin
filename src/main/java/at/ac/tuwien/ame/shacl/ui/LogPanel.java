package at.ac.tuwien.ame.shacl.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;

public class LogPanel extends JPanel {

	private static final long serialVersionUID = -7480637999509009997L;
	private JTextArea textArea = new JTextArea("Hi. I'm a log. I will let you know, what's happening.");
	private OWLModelManager modelManager;
	private static final Logger log = Logger.getLogger(QueryPanel.class);
    
    public LogPanel(OWLModelManager modelManager) {
    	setLayout(new BorderLayout());

    	this.modelManager = modelManager;
        //recalculate();
        log.info("model manager set");
        textArea.setEditable(false);
        textArea.setEnabled(true);
        JScrollPane scroll = new JScrollPane(textArea); 
        add(scroll, BorderLayout.CENTER);
    }
    
    public void dispose() {
        //modelManager.removeListener(modelListener);
        //refreshButton.removeActionListener(refreshAction);
    }
}

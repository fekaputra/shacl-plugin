package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;

import at.ac.tuwien.shacl.plugin.ui.util.ShaclCallbackListener;
import at.ac.tuwien.shacl.plugin.ui.util.ShaclCallbackNotifier;

public class ShaclConstraintViolationPanel extends JPanel implements ShaclCallbackListener {

	private static final long serialVersionUID = -7480637999509009997L;
	private JTextArea textArea = new JTextArea();
	private static final Logger log = Logger.getLogger(QueryPanel.class);
    
    public ShaclConstraintViolationPanel(OWLModelManager modelManager) {
    	setLayout(new BorderLayout());

        textArea.setEditable(false);
        textArea.setEnabled(true);
        JScrollPane scroll = new JScrollPane(textArea); 
        add(scroll, BorderLayout.CENTER);
        ShaclCallbackNotifier.register(this);
    }
    
    public void dispose() {
    }

	@Override
	public void handleMessage(String message) {
		textArea.setText(message);
	}
}

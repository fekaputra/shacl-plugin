package at.ac.tuwien.shacl.plugin.ui;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import com.hp.hpl.jena.rdf.model.Model;
import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;

import javax.swing.*;
import java.awt.*;
import java.io.StringWriter;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class ShaclConstraintViolationPanel extends JPanel {

	private static final long serialVersionUID = -7480637999509009997L;
	private JTextArea textArea = new JTextArea();
	private static final Logger log = Logger.getLogger(ShaclConstraintViolationPanel.class);

    private Observer shaclObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            Model model = (Model) arg;

            StringWriter out = new StringWriter();
            model.write(out, "TURTLE");

            if(model.isEmpty()) {
                textArea.setText("no violations detected");
            } else {
                textArea.setText(out.toString());
            }

        }
    };

    private Observer errorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            textArea.setText(arg.toString());
        }
    };

    public ShaclConstraintViolationPanel(OWLModelManager modelManager) {
        this.initObservers();

    	setLayout(new BorderLayout());

        textArea.setEditable(false);
        textArea.setEnabled(true);
        JScrollPane scroll = new JScrollPane(textArea); 
        add(scroll, BorderLayout.CENTER);
    }

    private void initObservers() {
        //register to events from shacl validation
        ShaclValidationRegistry.addObserver(shaclObserver);

        //register to error events emitted by this project
        ErrorNotifier.register(errorObserver);
    }

    public void dispose() {
        ShaclValidationRegistry.removeObserver(shaclObserver);
        ErrorNotifier.unregister(errorObserver);
    }
}

package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.jena.query.QueryException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RiotException;
import org.apache.jena.util.FileUtils;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.topbraid.spin.util.JenaUtil;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;
import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;

public class ShaclEditorPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;

    private JButton execButton = new JButton("Validate");
    private JTextPane editorPane = new JTextPane();
    private OWLModelManager modelManager;

    private ActionListener execButtonAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            validateGraph();
        }
    };

    // private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
    // public void handleChange(OWLModelManagerChangeEvent event) {
    // if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
    // // recalculate();
    // }
    // }
    // };

    public ShaclEditorPanel(OWLModelManager modelManager) {
        this.init(modelManager);
    }

    private void init(OWLModelManager modelManager) {
        // init shacl validator in its own thread, because it takes a while
        // this.thread = new Thread(new SHACLValidatorInitializer());
        // thread.start();

        this.setLayout(new BorderLayout());

        Font font = new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize());

        // set model manager
        this.modelManager = modelManager;

        // add text editor related functionality
        JScrollPane scroll = new JScrollPane(editorPane);
        add(scroll, BorderLayout.CENTER);

        editorPane.setFont(font);
        editorPane.setText(ShaclModelFactory.getExampleModelAsString() + "\n ###### add SHACL vocabulary ###### \n");

        // add "Execute" button related functionality
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
            Model dataModel = converter.ModelOwlToJenaConvert(ont, "TURTLE");

            // Load the main data model
            Model shapesModel = JenaUtil.createDefaultModel();
            shapesModel.read(new ByteArrayInputStream(editorPane.getText().getBytes()), null, FileUtils.langTurtle);
            ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        } catch (RiotException e) {
            ErrorNotifier.notify(e.getLocalizedMessage());
        } catch (QueryException e) {
            ErrorNotifier.notify("Encountered query error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotifier.notify("Something went wrong. Please check the error log for more information.");
        }
    }

    public void dispose() {
    }

    // private class SHACLValidatorInitializer implements Runnable {
    // @Override
    // public void run() {
    // ModelConstraintValidator.get();
    //// SHACLValidator validator = SHACLValidator.getDefaultValidator();
    //// ShaclEditorPanel.this.validator = validator;
    // }
    // }
}

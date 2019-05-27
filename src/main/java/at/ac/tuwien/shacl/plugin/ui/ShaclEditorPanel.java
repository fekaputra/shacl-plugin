package at.ac.tuwien.shacl.plugin.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.apache.jena.query.QueryException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RiotException;
import org.apache.jena.util.FileUtils;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLOntology;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;
import at.ac.tuwien.shacl.plugin.syntax.ShaclModelFactory;
import at.ac.tuwien.shacl.plugin.ui.template.EditorPanelTemplate;
import at.ac.tuwien.shacl.plugin.util.InferredOntologyLoader;
import at.ac.tuwien.shacl.plugin.util.RdfModelReader;

public class ShaclEditorPanel extends EditorPanelTemplate {
    private static final long serialVersionUID = -2739474730975140803L;

    private final Preferences preferences = PreferencesManager.getInstance().getPreferencesForSet("at.ac.tuwien.shacl.plugin", "shaclEditorPanel");

    private final OWLModelManager modelManager;

    private AbstractAction execButtonAction = new AbstractAction("Validate") {
        @Override
        public void actionPerformed(ActionEvent e) {
            validateGraph();
        }
    };

    @Override
    protected Iterable<Action> getActions() {
        List<Action> actions = new ArrayList<>();

        Iterable<Action> editorActions = super.getActions();

        editorActions.forEach(actions::add);

        // TODO: add separator?

        actions.add(this.execButtonAction);
        return actions;
    }

    // private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
    // public void handleChange(OWLModelManagerChangeEvent event) {
    // if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
    // // recalculate();
    // }
    // }
    // };

    public ShaclEditorPanel(OWLModelManager modelManager) {
        super();

        this.modelManager = modelManager;

        this.init();
    }

    @Override
    protected void init() {
        super.init();

        // init shacl validator in its own thread, because it takes a while
        // this.thread = new Thread(new SHACLValidatorInitializer());
        // thread.start();

        String currentDirectory = preferences.getString("currentDirectory", fileChooser.getFileSystemView().getDefaultDirectory().getAbsolutePath());
        fileChooser.setCurrentDirectory(new File(currentDirectory));

        try {
            this.setEditorText(ShaclModelFactory.getExampleModelAsString() + "\n ###### add SHACL vocabulary ###### \n");
        } catch (IOException e) {
            e.printStackTrace();
            this.setEditorText("error loading Example Model");
        }
    }

    private void validateGraph() {
        try {
            JenaOwlConverter converter = new JenaOwlConverter();

            OWLOntology ont = InferredOntologyLoader.loadInferredOntology(this, modelManager);
            Model dataModel = converter.ModelOwlToJenaConvert(ont, FileUtils.langTurtle);

            // Load the main data model
            Model shapesModel = RdfModelReader.getModelFromString(this.getEditorText(), FileUtils.langTurtle);
            ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        } catch (RiotException e) {
            ErrorNotifier.notify("Encountered parsing error: " + e.getLocalizedMessage());
        } catch (QueryException e) {
            ErrorNotifier.notify("Encountered query error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ErrorNotifier.notify("Something went wrong. Please check the error log for more information.");
        }
    }

    public void dispose() {
        preferences.putString("currentDirectory", fileChooser.getCurrentDirectory().getAbsolutePath());
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

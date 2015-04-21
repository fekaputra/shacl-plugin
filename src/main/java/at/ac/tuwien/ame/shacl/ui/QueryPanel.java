package at.ac.tuwien.ame.shacl.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class QueryPanel extends JPanel {
    private static final long serialVersionUID = -2739474730975140803L;
    private JButton execButton = new JButton("Execute");
    private JTextArea editorTextArea = new JTextArea("I'm a text. Please scroll me.");
    private OWLModelManager modelManager;
    private static final Logger log = Logger.getLogger(QueryPanel.class);

    private ActionListener refreshAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // recalculate();
        }
    };

    private OWLModelManagerListener modelListener = new OWLModelManagerListener() {
        public void handleChange(OWLModelManagerChangeEvent event) {
            if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
                // recalculate();
            }
        }
    };

    public QueryPanel(OWLModelManager modelManager) {
        setLayout(new BorderLayout());

        log.info("in editor panel");
        this.modelManager = modelManager;
        // recalculate();
        log.info("model manager set");
        modelManager.addListener(modelListener);
        // refreshButton.addActionListener(refreshAction);
        log.info("listener added");
        JScrollPane scroll = new JScrollPane(editorTextArea);

        add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(execButton, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

        execButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // transformSpinToSparql(editorTextArea.getText());
                execute();
            }
        });

        log.info("text component added");

    }

    private void execute() {
        StringBuffer sb = new StringBuffer();
        OWLOntology ont = modelManager.getActiveOntology();
        Collection<OWLAxiom> axioms = ont.getTBoxAxioms(false);
        Iterator<OWLAxiom> axiomsIt = axioms.iterator();
        while (axiomsIt.hasNext()) {
            OWLAxiom axiom = axiomsIt.next();
            sb.append(axiom);
        }
        if (sb.toString().isEmpty()) {
            editorTextArea.setText("Empty Ontology");
        } else {
            editorTextArea.setText(sb.toString());
        }
    }

    private void transformSpinToSparql(String spinString) {

        // spin initialization
        // SPINModuleRegistry.get().init();

        // convert string into byteArray for initialization
        // InputStream spinIS = new ByteArrayInputStream(spinString.getBytes(StandardCharsets.UTF_8));

        // jena model initialization
        // Model model = ModelFactory.createDefaultModel();
        // model.read(spinIS, null);

        // write the result out
        // RDFDataMgr.write(System.out, model, Lang.TURTLE);

    }

    public void dispose() {
        modelManager.removeListener(modelListener);
        // refreshButton.removeActionListener(refreshAction);
    }
}

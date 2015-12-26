package at.ac.tuwien.shacl.plugin.ui;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import org.apache.log4j.Logger;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.prefix.PrefixUtilities;
import org.topbraid.shacl.vocabulary.SH;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Panel for the constraint violations.
 */
public class ShaclConstraintViolationPanel extends JPanel {

	private static final long serialVersionUID = -7480637999509009997L;
	private static final Logger log = Logger.getLogger(ShaclConstraintViolationPanel.class);

    private OWLModelManager modelManager;

    /**
     * Table showing the constraint violations.
     */
    private JTable table;

    //TODO show URIs with prefixes -> issue: result model doesn't contain prefixes,fetch them from somewhere else
    //TODO link table selection with events
    private Observer shaclObserver = new Observer() {
        /**
         * Called, when the SHACL validator was executed, and the results were returned.
         *
         * @param o observable notifying the observer
         * @param arg result model fetched from Jena
         */
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("axioms: "+modelManager.getActiveOntology().getAxiomCount());
            //TODO doesn't work, prefixes always empty
            System.out.println(modelManager.getOWLOntologyManager().getOntologyFormat(modelManager.getActiveOntology()).asPrefixOWLOntologyFormat().getPrefixNames());

            Model model = (Model) arg;

            //clear table
            ((DefaultTableModel) table.getModel()).setRowCount(0);

            //update table with data from jena model
            for(Statement stmt : model.listStatements(null, RDF.type, SH.ValidationResult).toList()) {
                Vector<String> row = new Vector<>();
                Resource subject = stmt.getSubject();

                row.add(subject.getProperty(SH.message).getObject().toString());
                row.add(subject.getProperty(SH.focusNode).getObject().toString());
                row.add(subject.getProperty(SH.subject).getObject().toString());
                row.add(subject.getProperty(SH.predicate).getObject().toString());
                row.add(subject.getProperty(SH.object).getObject().toString());
                row.add(subject.getProperty(SH.severity).getObject().toString());
                row.add(subject.getProperty(SH.sourceConstraint).getObject().toString());
                row.add(subject.getProperty(SH.sourceShape).getObject().toString());
                row.add(subject.getProperty(SH.sourceTemplate).getObject().toString());

                ((DefaultTableModel)table.getModel()).addRow(row);
            }
        }
    };

    private Observer errorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            //textArea.setText(arg.toString());
        }
    };

    public ShaclConstraintViolationPanel(OWLModelManager modelManager) {
        this.modelManager = modelManager;

        String[] headers = {
                "Message", "FocusNode", "Subject", "Object", "Predicate", "Severity", "SourceConstraint",
                "SourceShape", "SourceTemplate"
        };

        String[][] data = {};

        TableModel tableModel = new DefaultTableModel(data, headers);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.initObservers();

    	setLayout(new BorderLayout());

        table.setAutoscrolls(true);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Initialize all services this class wants to subscribe.
     */
    private void initObservers() {
        //register to events from shacl validation
        ShaclValidationRegistry.addObserver(shaclObserver);

        //register to error events emitted by this project
        ErrorNotifier.register(errorObserver);
    }

    /**
     * Defines behavior on disposal of panel.
     */
    public void dispose() {
        ShaclValidationRegistry.removeObserver(shaclObserver);
        ErrorNotifier.unregister(errorObserver);
    }
}

package at.ac.tuwien.shacl.plugin.ui;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import org.protege.editor.owl.model.OWLModelManager;
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
    /**
     * Table view showing the constraint violations.
     */
    private JTable table;

    /**
     * Get a qualified name for an URI, if it exists, otherwise just return the original string.
     *
     * @param model Model containing the prefixes
     * @param string String to be checked for a qname
     * @return qname if one exists, otherwise the original string
     */
    private String getQName(Model model, String string) {
        return model.qnameFor(string) == null ? string : model.qnameFor(string);
    }

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
            Model model = (Model) arg;

            //clear table
            ((DefaultTableModel) table.getModel()).setRowCount(0);

            //update table with data from jena model
            for(Statement stmt : model.listStatements(null, RDF.type, SH.ValidationResult).toList()) {
                Vector<String> row = new Vector<>();
                Resource subject = stmt.getSubject();

                row.add(getQName(model, subject.getProperty(SH.message).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.focusNode).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.subject).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.predicate).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.object).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.severity).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.sourceConstraint).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.sourceShape).getObject().toString()));
                row.add(getQName(model, subject.getProperty(SH.sourceTemplate).getObject().toString()));

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
        System.out.println(modelManager.getActiveOntology().getAxioms());

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

    	setLayout(new BorderLayout());

        table.setAutoscrolls(true);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        this.initObservers();
    }

    /**
     *  Register to all services this class wants to subscribe.
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

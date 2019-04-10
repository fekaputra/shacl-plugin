package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.protege.editor.owl.model.OWLModelManager;
import org.topbraid.shacl.vocabulary.SH;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;

/**
 * Panel for the constraint violations.
 */
public class ShaclConstraintViolationPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1093799641840761261L;

    /**
     * Table view showing the constraint violations.
     */
    private JTable table;

    /**
     * Get a qualified name for an URI, if it exists, otherwise just return the original string.
     *
     * @param model Model containing the prefixes
     * @param stmt Statement to be checked for a qname
     * @return qname if one exists, otherwise the original string of the object
     */
    private String getQName(Model model, Statement stmt) {
        if (stmt != null && stmt.getObject() != null) {
            String string = stmt.getObject().toString();
            return model.qnameFor(string) == null ? string : model.qnameFor(string);
        } else {
            return "";
        }
    }

    // TODO link table selection with events
    /**
     * Defines behavior when object gets notified about a SHACL validation result. Shows the constraint violations of
     * the result Jena model in the table view.
     */
    private Observer shaclObserver = new Observer() {
        /**
         * Called, when the SHACL validator was executed, and the results were returned.
         *
         * @param o observable notifying the observer
         * @param arg result model fetched from Jena
         */
        @Override
        public void update(Observable o, Object arg) {
            Model model = ((Resource) arg).getModel();

            // clear table
            ((DefaultTableModel) table.getModel()).setRowCount(0);

            //Resource report = model.listStatements(null, RDF.type, SH.ValidationReport).toList().get(0).getSubject();
            //boolean conforms = report.getProperty(SH.conforms).getObject().asLiteral().getBoolean();
            // TODO: indicate whether it conforms or not

            // update table with data from jena model
            for (Statement stmt : model.listStatements(null, RDF.type, SH.ValidationResult).toList()) {
                Vector<String> row = new Vector<>();
                Resource subject = stmt.getSubject();

                row.add(getQName(model, subject.getProperty(SH.resultMessage)));
                row.add(getQName(model, subject.getProperty(SH.focusNode)));
                row.add(getQName(model, subject.getProperty(SH.resultPath)));
                row.add(getQName(model, subject.getProperty(SH.sourceShape)));
                row.add(getQName(model, subject.getProperty(SH.resultSeverity)));

                // TODO: display value
                //row.add(getQName(model, subject.getProperty(SH.value)));

                ((DefaultTableModel) table.getModel()).addRow(row);
            }
        }
    };

    private Observer errorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            // textArea.setText(arg.toString());
        }
    };

    /**
     *
     * @param modelManager
     */
    public ShaclConstraintViolationPanel(OWLModelManager modelManager) {
        this.init();
    }

    protected void init() {
        // System.out.println(modelManager.getActiveOntology().getAxioms());

        String[] headers = { "Message", "FocusNode", "Path", "SourceShape", "Severity" };
        String[][] data = {};

        TableModel tableModel = new DefaultTableModel(data, headers);
        table = new JTable(tableModel) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setAutoscrolls(true);

        JScrollPane scroll = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);

        this.initObservers();
    }

    /**
     * Register to all services this class wants to subscribe.
     */
    private void initObservers() {
        // register to events from shacl validation
        ShaclValidationRegistry.addObserver(shaclObserver);

        // register to error events emitted by this project
        ErrorNotifier.register(errorObserver);
    }

    /**
     * Defines behavior on disposal of panel.
     */
    public void dispose() {
        ShaclValidationRegistry.removeObserver(shaclObserver);
        ErrorNotifier.unregister(errorObserver);
    }

    public DefaultTableModel getTableModel() {
        return ((DefaultTableModel) table.getModel());
    }

    public JTable getTable() {
        return this.table;
    }
}

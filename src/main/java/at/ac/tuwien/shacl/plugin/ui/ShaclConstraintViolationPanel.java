package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.protege.editor.owl.model.OWLModelManager;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;
import at.ac.tuwien.shacl.plugin.util.ShaclValidationReport;
import at.ac.tuwien.shacl.plugin.util.ShaclValidationResult;

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
            updateTable((ShaclValidationReport) arg);
        }
    };

    private Observer errorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            // textArea.setText(arg.toString());
        }
    };

    private void updateTable(ShaclValidationReport report) {
        // clear table
        ((DefaultTableModel) table.getModel()).setRowCount(0);

        // TODO: indicate whether it conforms or not

        List<ShaclValidationResult> validationResults = new ArrayList<>(report.validationResults);
        validationResults.sort(null);

        // update table with result data
        for (ShaclValidationResult res : validationResults) {
            Vector<String> row = new Vector<>();

            row.add(res.resultSeverity.toString());
            row.add(JenaOwlConverter.getQName(res.model, res.sourceShape));
            row.add(JenaOwlConverter.getQName(res.model, res.resultMessage));
            row.add(JenaOwlConverter.getQName(res.model, res.focusNode));
            row.add(JenaOwlConverter.getQName(res.model, res.resultPath));
            row.add(JenaOwlConverter.getQName(res.model, res.value));

            ((DefaultTableModel) table.getModel()).addRow(row);
        }
    }

    /**
     *
     * @param modelManager
     */
    public ShaclConstraintViolationPanel(OWLModelManager modelManager) {
        this.init();
    }

    protected void init() {
        // System.out.println(modelManager.getActiveOntology().getAxioms());

        String[] headers = { "Severity", "SourceShape", "Message", "FocusNode", "Path", "Value" };
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

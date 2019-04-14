package at.ac.tuwien.shacl.plugin;

import java.io.FileNotFoundException;

import org.apache.jena.rdf.model.Model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import at.ac.tuwien.shacl.plugin.ui.ShaclConstraintViolationPanel;
import at.ac.tuwien.shacl.plugin.util.TestUtil;

/**
 * Tests the constraint violation panel.
 */
public class TestConstraintViolationPanel {
    /**
     * Test, if table is indeed updated with new data after a SHACL validation.
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void testTableUpdateAfterValidation() throws FileNotFoundException, InterruptedException {
        ShaclConstraintViolationPanel panel = new ShaclConstraintViolationPanel(null);
        assertEquals(0, panel.getTableModel().getRowCount());

        Model dataModel   = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        assertEquals(3, panel.getTableModel().getRowCount());
        assertNotNull(panel.getTableModel().getValueAt(0, 0));
        assertNotNull(panel.getTableModel().getValueAt(1, 0));
        assertNotNull(panel.getTableModel().getValueAt(2, 0));
    }

    @Test
    public void testBehaviorAfterInit() throws FileNotFoundException, InterruptedException {
        ShaclConstraintViolationPanel panel = new ShaclConstraintViolationPanel(null);

        Model dataModel   = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        assertFalse(panel.getTable().isCellEditable(0, 0));
    }

    @Test
    public void testDispose() throws FileNotFoundException, InterruptedException {
        ShaclConstraintViolationPanel panel = new ShaclConstraintViolationPanel(null);

        Model dataModel   = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        assertEquals(3, panel.getTableModel().getRowCount());

        panel.getTableModel().setRowCount(0);
        panel.dispose();

        Model dataModel2   = TestUtil.getDataModel();
        Model shapesModel2 = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel2, dataModel2);

        assertEquals(0, panel.getTableModel().getRowCount());
    }
}

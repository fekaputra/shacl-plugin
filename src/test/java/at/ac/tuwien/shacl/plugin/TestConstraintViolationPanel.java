package at.ac.tuwien.shacl.plugin;

import at.ac.tuwien.shacl.plugin.events.ShaclValidationRegistry;
import at.ac.tuwien.shacl.plugin.ui.ShaclConstraintViolationPanel;
import at.ac.tuwien.shacl.plugin.util.TestUtil;
import org.apache.jena.rdf.model.Model;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

/**
 * Tests the constraint violation panel.
 */
public class TestConstraintViolationPanel {
    /**
     * Test, if table is indeed updated with new data after a SHACL validation.
     *
     * @throws IOException
     */
    @Test public void testTableUpdateAfterValidation() throws IOException {
        ShaclConstraintViolationPanel panel = new ShaclConstraintViolationPanel();
        assertEquals(0, panel.getTableModel().getRowCount());

        Model dataModel = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        // there must be three violations
        assertEquals(3, panel.getTableModel().getRowCount());

        // Bob is not allowed to have two values for ssn
        assertEquals("sh:Violation", panel.getTableModel().getValueAt(0, 0));
        assertNotNull(panel.getTableModel().getValueAt(0, 2));
        assertThat(panel.getTableModel().getValueAt(0, 2).toString(), containsString("may only have"));
        assertEquals("ex:Bob", panel.getTableModel().getValueAt(0, 3));
        assertEquals("ex:ssn", panel.getTableModel().getValueAt(0, 4));

        // Calvin is not allowed to have a birth date, as the shape is closed
        assertEquals("sh:Violation", panel.getTableModel().getValueAt(1, 0));
        assertNotNull(panel.getTableModel().getValueAt(1, 2));
        assertThat(panel.getTableModel().getValueAt(1, 2).toString(), containsString("closed shape"));
        assertEquals("ex:Calvin", panel.getTableModel().getValueAt(1, 3));
        assertEquals("ex:birthDate", panel.getTableModel().getValueAt(1, 4));

        // Alice has an invalid ssn
        assertEquals("sh:Warning", panel.getTableModel().getValueAt(2, 0));
        assertNotNull(panel.getTableModel().getValueAt(2, 2));
        assertThat(panel.getTableModel().getValueAt(2, 2).toString(), containsString("match pattern"));
        assertEquals("ex:Alice", panel.getTableModel().getValueAt(2, 3));
        assertEquals("ex:ssn", panel.getTableModel().getValueAt(2, 4));
    }

    @Test public void testBehaviorAfterInit() throws IOException {
        ShaclConstraintViolationPanel panel = new ShaclConstraintViolationPanel();

        Model dataModel = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        assertFalse(panel.getTable().isCellEditable(0, 0));
    }

    @Test public void testDispose() throws IOException {
        ShaclConstraintViolationPanel panel = new ShaclConstraintViolationPanel();

        Model dataModel = TestUtil.getDataModel();
        Model shapesModel = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel, dataModel);

        assertEquals(3, panel.getTableModel().getRowCount());

        panel.getTableModel().setRowCount(0);
        panel.dispose();

        Model dataModel2 = TestUtil.getDataModel();
        Model shapesModel2 = TestUtil.getShapesModel();

        ShaclValidationRegistry.getValidator().runValidation2(shapesModel2, dataModel2);

        assertEquals(0, panel.getTableModel().getRowCount());
    }
}

package at.ac.tuwien.shacl.plugin.ui;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

import java.awt.*;

/**
 * Link to the plugin.xml for the constraint violation table.
 */
public class ShaclConstraintViolationViewComponent extends AbstractOWLViewComponent {
    private static final long serialVersionUID = 3718949171901002345L;
    private ShaclConstraintViolationPanel violationsPanel;

    @Override protected void initialiseOWLView() throws Exception {
        violationsPanel = new ShaclConstraintViolationPanel(this);

        this.setLayout(new BorderLayout());
        this.add(violationsPanel, BorderLayout.CENTER);
    }

    @Override protected void disposeOWLView() {
        violationsPanel.dispose();
    }
}

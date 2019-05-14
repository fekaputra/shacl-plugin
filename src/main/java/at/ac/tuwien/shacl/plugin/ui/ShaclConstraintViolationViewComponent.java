package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.view.AbstractActiveOntologyViewComponent;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Link to the plugin.xml for the constraint violation table.
 */
public class ShaclConstraintViolationViewComponent extends AbstractOWLViewComponent {
	private static final long serialVersionUID = 3718949171901002345L;
	private ShaclConstraintViolationPanel violationsPanel;

	@Override
	protected void initialiseOWLView() throws Exception {
		violationsPanel = new ShaclConstraintViolationPanel();

		this.setLayout(new BorderLayout());
		this.add(violationsPanel, BorderLayout.CENTER);
	}

	@Override
	protected void disposeOWLView() {
		violationsPanel.dispose();
	}
}

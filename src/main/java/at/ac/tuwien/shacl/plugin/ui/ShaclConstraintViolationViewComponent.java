package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.view.AbstractActiveOntologyViewComponent;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLOntology;

public class ShaclConstraintViolationViewComponent extends AbstractActiveOntologyViewComponent {
	private static final long serialVersionUID = 3718949171901002345L;
	private static final Logger log = Logger.getLogger(ShaclConstraintViolationViewComponent.class);
	private ShaclConstraintViolationPanel logPanel;

	@Override
	protected void initialiseOntologyView() throws Exception {
		setLayout(new BorderLayout());
		logPanel = new ShaclConstraintViolationPanel(getOWLModelManager());

		add(logPanel, BorderLayout.CENTER);
	}

	@Override
	protected void disposeOntologyView() {
		logPanel.dispose();
	}

	@Override
	protected void updateView(OWLOntology activeOntology) throws Exception {

	}
}

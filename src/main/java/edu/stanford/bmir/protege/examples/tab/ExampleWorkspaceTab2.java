package edu.stanford.bmir.protege.examples.tab;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;

public class ExampleWorkspaceTab2 extends OWLWorkspaceViewsTab {
	private static final Logger log = Logger.getLogger(ExampleWorkspaceTab2.class);
	private static final long serialVersionUID = -4896884982262745722L;

	public ExampleWorkspaceTab2() {
		setToolTipText("Custom tooltip text for Example Tab (2)");
	}

    @Override
	public void initialise() {
		super.initialise();
		log.info("Example Workspace Tab (2) initialized");
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Example Workspace Tab (2) disposed");
	}
}

package at.ac.tuwien.shacl.plugin.ui;


import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;

public class ShaclMinimalWorkspaceTab extends OWLWorkspaceViewsTab {
	private static final Logger log = Logger.getLogger(ShaclMinimalWorkspaceTab.class);
	private static final long serialVersionUID = -4896884982262745722L;

	public ShaclMinimalWorkspaceTab() {
		setToolTipText("SHACL Minimal Editor");
	}

    @Override
	public void initialise() {
		super.initialise();
		log.info("SHACL Minimal Editor initialized");
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("SHACL Minimal Editor disposed");
	}
}

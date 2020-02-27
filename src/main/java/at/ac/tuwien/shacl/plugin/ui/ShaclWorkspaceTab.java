package at.ac.tuwien.shacl.plugin.ui;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;

public class ShaclWorkspaceTab extends OWLWorkspaceViewsTab {
    private static final Logger log = Logger.getLogger(ShaclMinimalWorkspaceTab.class);
    private static final long serialVersionUID = -4896884982262745722L;

    public ShaclWorkspaceTab() {
        setToolTipText("SHACL Editor");
    }

    @Override public void initialise() {
        super.initialise();
        log.info("SHACL Editor initialized");
    }

    @Override public void dispose() {
        super.dispose();
        log.info("SHACL Editor disposed");
    }
}

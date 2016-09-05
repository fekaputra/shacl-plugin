package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

/**
 * Links the SHACL text editor view with the plugin.xml file.
 */
public class ShaclEditorViewComponent extends AbstractOWLViewComponent {
    private static final long serialVersionUID = 3718949171901002345L;
    private ShaclEditorPanel editorPanel;

    @Override
    protected void initialiseOWLView() throws Exception {
        setLayout(new BorderLayout());
        editorPanel = new ShaclEditorPanel(getOWLModelManager());
        add(editorPanel, BorderLayout.CENTER);
    }

    @Override
    protected void disposeOWLView() {
        editorPanel.dispose();
    }
}

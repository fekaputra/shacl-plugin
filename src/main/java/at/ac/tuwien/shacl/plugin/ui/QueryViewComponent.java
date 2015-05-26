package at.ac.tuwien.shacl.plugin.ui;

import java.awt.BorderLayout;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

public class QueryViewComponent extends AbstractOWLViewComponent {

	private static final long serialVersionUID = 3718949171901002345L;
	private static final Logger log = Logger.getLogger(QueryViewComponent.class);
	private QueryPanel editorPanel;

	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new BorderLayout());
		editorPanel = new QueryPanel(getOWLModelManager());
		add(editorPanel, BorderLayout.CENTER);
	}
	
	@Override
	protected void disposeOWLView() {
		editorPanel.dispose();
	}
}

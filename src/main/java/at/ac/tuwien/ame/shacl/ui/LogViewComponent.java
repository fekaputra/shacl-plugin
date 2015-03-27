package at.ac.tuwien.ame.shacl.ui;

import java.awt.BorderLayout;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

public class LogViewComponent extends AbstractOWLViewComponent {
	private static final long serialVersionUID = 3718949171901002345L;
	private static final Logger log = Logger.getLogger(QueryViewComponent.class);
	private LogPanel logPanel;

	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new BorderLayout());
		logPanel = new LogPanel(getOWLModelManager());
		add(logPanel, BorderLayout.CENTER);
	}
	
	@Override
	protected void disposeOWLView() {
		logPanel.dispose();
	}
}

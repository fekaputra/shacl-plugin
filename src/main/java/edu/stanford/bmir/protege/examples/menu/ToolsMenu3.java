package edu.stanford.bmir.protege.examples.menu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import org.protege.editor.owl.ui.action.ProtegeOWLAction;

public class ToolsMenu3 extends ProtegeOWLAction {
	private static final long serialVersionUID = 1439595221948942812L;
	public static Logger LOGGER = Logger.getLogger(ToolsMenu3.class);

	public void initialise() throws Exception {
	}

	public void dispose() throws Exception {
	}

	public void actionPerformed(ActionEvent event) {
		StringBuffer message = new StringBuffer(
				"This example menu item is under the Tools menu, but displayed in a separate category from the other example menu items.\n");
		message.append("The active ontology has ");
		message.append(getOWLModelManager().getActiveOntology().getClassesInSignature().size());
		message.append(" classes.");
		JOptionPane.showMessageDialog(getOWLWorkspace(), message.toString());	
	}
}

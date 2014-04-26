package edu.stanford.bmir.protege.examples.menu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.protege.editor.owl.ui.action.ProtegeOWLAction;

public class UnderSubMenu extends ProtegeOWLAction {
	private static final long serialVersionUID = 2452385628012488942L;

	public void initialise() throws Exception {
	}

	public void dispose() throws Exception {
	}

	public void actionPerformed(ActionEvent arg0) {
		StringBuffer message = new StringBuffer("Yet another example menu item.\n");
		message.append("The active ontology has ");
		message.append(getOWLModelManager().getActiveOntology().getObjectPropertiesInSignature().size());
		message.append(" object properties.");
		JOptionPane.showMessageDialog(getOWLWorkspace(), message.toString());
	}
}

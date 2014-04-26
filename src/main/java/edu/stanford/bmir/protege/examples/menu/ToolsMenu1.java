package edu.stanford.bmir.protege.examples.menu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.protege.editor.owl.ui.action.ProtegeOWLAction;

public class ToolsMenu1 extends ProtegeOWLAction {
	private static final long serialVersionUID = -2896209622461162777L;

	public void initialise() throws Exception {
	}

	public void dispose() throws Exception {
	}

	public void actionPerformed(ActionEvent event) {
		StringBuffer message = new StringBuffer("This example menu item is under the Tools menu.\n");
		message.append("The currently selected class is ");
		message.append(getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass());
		message.append(".");
		JOptionPane.showMessageDialog(getOWLWorkspace(), message.toString());	
	}
}

package edu.stanford.bmir.protege.examples.menu;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import org.protege.editor.owl.ui.action.ProtegeOWLAction;

public class ToolsMenu2 extends ProtegeOWLAction {
	private static final long serialVersionUID = -5859609615256451118L;
	public static Logger LOGGER = Logger.getLogger(ToolsMenu2.class);

	public void initialise() throws Exception {
	}

	public void dispose() throws Exception {
	}

	public void actionPerformed(ActionEvent event) {
		StringBuffer message = new StringBuffer("This is the second example menu item under the Tools menu.\n");
		message.append("The active ontology has ");
		message.append(getOWLModelManager().getActiveOntology().getAxiomCount());
		message.append(" axioms.");
		JOptionPane.showMessageDialog(getOWLWorkspace(), message.toString());	
	}
}

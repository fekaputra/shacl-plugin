package at.ac.tuwien.shacl.plugin.ui;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;

public class ErrorDialog extends ProtegeOWLAction {
	private static final long serialVersionUID = 1439595221948942812L;

	public void initialise() throws Exception {
	}

	public void dispose() throws Exception {
	}
	
	public void showDialog(String message) {
		JOptionPane.showMessageDialog(getOWLWorkspace(), message);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}

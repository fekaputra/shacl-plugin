package at.ac.tuwien.shacl.plugin.ui;

import at.ac.tuwien.shacl.plugin.ui.template.ListPanelTemplate;
import org.protege.editor.owl.ui.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;

/**
 * Prototype implementation of the constraint picker view.
 *
 * //TODO use class with real data.
 */
public class ShaclConstraintPickerPanel extends ListPanelTemplate {
    private JFileChooser fileChooser = new JFileChooser();

    public ShaclConstraintPickerPanel() {
        super();
        super.init();

        fileChooser.setMultiSelectionEnabled(true);
    }

    @Override
    protected Iterable<Action> getActions() {
        List<Action> actions = new ArrayList<>();

        actions.add(new AddConstraintAction("Add constraint", OWLIcons.getIcon("individual.add.png")));
        actions.add(new DeleteConstraintAction("Delete constraint", OWLIcons.getIcon("individual.delete.png")));
        actions.add(new ImportConstraintFileAction("Import constraint", OWLIcons.getIcon("DefinedOWLClass.gif")));

        return actions;
    }

    /**
     * Defines the add constraint action and its behavior, when user clicks on it.
     */
    private class AddConstraintAction extends AbstractAction {
        public AddConstraintAction(String name, Icon icon) {
            super(name, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, "Add constraint definition");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //generate random value and add them to list
            String[] data = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven"};
            String selected = data[(int) (Math.random()*10)];
            getListPublisher().addElement(selected);
        }
    }

    /**
     * Defines the delete constraint action and its behavior, when user clicks on it.
     * Registers itself as observer to the defaultListModel, so it can automatically register when the model is
     * changed. This is done, so the action can disable itself, when the model is empty.
     */
    private class DeleteConstraintAction extends AbstractAction implements Observer {
        public DeleteConstraintAction(String name, Icon icon) {
            super(name, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, "Delete constraint definition");
            getListPublisher().addObserver(this);
            if(getListPublisher().getSize() == 0) {
                this.setEnabled(false);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = getList().getSelectedIndex();
            if(index != -1) {
                getListPublisher().remove(index);
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            if((int) arg == 0) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        }
    }

    /**
     * Defines the import constraint action and its behavior, when user clicks on it.
     *
     * //TODO move import functionality to add constraint dialog
     */
    private class ImportConstraintFileAction extends AbstractAction {
        public ImportConstraintFileAction(String name, Icon icon) {
            super(name, icon);
            putValue(AbstractAction.SHORT_DESCRIPTION, "Import constraint definition");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fileChooser.showOpenDialog(getList());

            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();

                for(File file : files) {
                    getListPublisher().addElement(file.getName());
                }
            }
        }
    }
}

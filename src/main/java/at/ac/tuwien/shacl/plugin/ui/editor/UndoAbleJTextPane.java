package at.ac.tuwien.shacl.plugin.ui.editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.undo.UndoManager;

// NOTE: inspired by https://stackoverflow.com/a/12030993/2565743

public class UndoAbleJTextPane extends JTextPane {

    public UndoAbleJTextPane() {
        UndoManager undoManager = new UndoManager();
        this.getDocument().addUndoableEditListener(undoManager);

        ActionMap actionMap = this.getActionMap();
        actionMap.put("Undo", new UndoAction(undoManager));
        actionMap.put("Redo", new RedoAction(undoManager));

        InputMap inputMap = this.getInputMap(WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");
    }

    private class UndoAction extends AbstractAction {
        private final UndoManager undoManager;

        UndoAction(UndoManager undoManager) {
            this.undoManager = undoManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
    }

    private class RedoAction extends AbstractAction {
        private final UndoManager undoManager;

        RedoAction(UndoManager undoManager) {
            this.undoManager = undoManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }
    }
}

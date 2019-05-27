package at.ac.tuwien.shacl.plugin.ui.template;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

import at.ac.tuwien.shacl.plugin.ui.editor.UndoAbleJTextPane;

/**
 * Creates a view, with a toolbar at the upper part and an text editor as the main part.
 */
public abstract class EditorPanelTemplate extends PanelToolbarTemplate {

    private JTextPane editorPane;

    public EditorPanelTemplate() {
        super();
    }

    protected void init() {
        super.init();

        // add text editor related functionality
        editorPane = new UndoAbleJTextPane();
        JScrollPane scroll = new JScrollPane(editorPane);
        this.add(scroll, BorderLayout.CENTER);

        Font font = new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize());

        editorPane.setFont(font);
    }

    @Override
    protected Iterable<Action> getActions() {
        return Collections.emptyList();
    }

    protected String getEditorText() {
        return this.editorPane.getText();
    }

    // TODO: extend Observable instead of direct setter?
    protected void setEditorText(String text) {
        this.editorPane.setText(text);
    }
}

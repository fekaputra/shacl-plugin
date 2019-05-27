package at.ac.tuwien.shacl.plugin.ui.template;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import at.ac.tuwien.shacl.plugin.events.ErrorNotifier;
import at.ac.tuwien.shacl.plugin.ui.editor.UndoAbleJTextPane;

/**
 * Creates a view, with a toolbar at the upper part and an text editor as the main part.
 */
public abstract class EditorPanelTemplate extends PanelToolbarTemplate {

    private final EditorPanelTemplate self = this;

    private JFileChooser fileChooser;

    private JTextPane editorPane;

    private AbstractAction openButtonAction = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int returnVal = fileChooser.showOpenDialog(self);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    self.openFile(file);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
                ErrorNotifier.notify("Unable to open file: " + ex.getLocalizedMessage());
            }
        }
    };

    private void openFile(File file) throws IOException {
        // TODO: read files with encoding different to the default one
        try(BufferedReader in = new BufferedReader(new FileReader(file))) {
            String newLine = System.getProperty("line.separator");
            String content = in.lines().collect(Collectors.joining(newLine));

            self.setEditorText(content);
        }
    }

    private AbstractAction saveButtonAction = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int returnVal = fileChooser.showSaveDialog(self);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    self.writeFile(file);
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
                ErrorNotifier.notify("Unable to write file: " + ex.getLocalizedMessage());
            }
        }
    };

    private void writeFile(File file) throws IOException {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            String content = self.getEditorText();
            out.write(content);
        }
    }

    public EditorPanelTemplate() {
        super();
    }

    protected void init() {
        super.init();

        fileChooser = new JFileChooser();
        // TODO: support multiple files?
        fileChooser.setMultiSelectionEnabled(false);

        // add text editor related functionality
        editorPane = new UndoAbleJTextPane();
        JScrollPane scroll = new JScrollPane(editorPane);
        this.add(scroll, BorderLayout.CENTER);

        Font font = new Font(Font.MONOSPACED, getFont().getStyle(), getFont().getSize());

        editorPane.setFont(font);
    }

    @Override
    protected Iterable<Action> getActions() {
        List<Action> actions = new ArrayList<>(2);
        actions.add(this.openButtonAction);
        actions.add(this.saveButtonAction);
        return actions;
    }

    protected String getEditorText() {
        return this.editorPane.getText();
    }

    // TODO: extend Observable instead of direct setter?
    protected void setEditorText(String text) {
        this.editorPane.setText(text);
    }
}

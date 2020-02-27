package at.ac.tuwien.shacl.plugin.ui.template;

import at.ac.tuwien.shacl.plugin.util.TypeChecker;

import javax.swing.*;
import java.awt.*;

/**
 * Create a JPanel with a toolbar attached on the upper part, where actions may be added.
 */
public abstract class PanelToolbarTemplate extends JPanel {
    private JToolBar toolbar;

    public PanelToolbarTemplate() {
    }

    protected void init() {
        toolbar = new JToolBar();

        this.setLayout(new BorderLayout());

        for (Action action : TypeChecker.safe(this.getActions())) {
            toolbar.add(action);
        }

        this.add(toolbar, BorderLayout.NORTH);
    }

    public JToolBar getToolbar() {
        return this.toolbar;
    }

    protected abstract Iterable<Action> getActions();
}

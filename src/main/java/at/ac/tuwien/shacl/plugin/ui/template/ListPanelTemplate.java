package at.ac.tuwien.shacl.plugin.ui.template;

import at.ac.tuwien.shacl.plugin.events.DefaultListPublisher;

import javax.swing.*;
import java.awt.*;

/**
 * Creates a view, with a toolbar at the upper part and a list as the main part.
 */
public abstract class ListPanelTemplate extends PanelToolbarTemplate {
    private JList<String> list;

    private DefaultListPublisher<String> publisherModel;

    public ListPanelTemplate() {
        super();

        publisherModel = new DefaultListPublisher<>();
    }

    protected void init() {
        super.init();

        list = new JList<>(publisherModel.getModel());
        JScrollPane scroll = new JScrollPane(list);

        this.add(scroll, BorderLayout.CENTER);
    }

    protected JList<String> getList() {
        return this.list;
    }

    protected DefaultListModel<String> getListModel() {
        return publisherModel.getModel();
    }

    protected DefaultListPublisher<String> getListPublisher() {
        return publisherModel;
    }
}

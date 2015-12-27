package at.ac.tuwien.shacl.plugin.events;

import javax.swing.*;
import java.util.Observable;

/**
 * Wraps the DefaultListModel with an Observable interface. This is done, so we can add actions/listeners and
 * while keeping all elements using the model decoupled from each other.
 *
 * @param <E>
 */
public class DefaultListPublisher<E> extends Observable {
    private DefaultListModel<E> model = new DefaultListModel<>();

    public void addElement(E element) {
        this.model.addElement(element);
        this.notifyWithListSize();
    }

    public void remove(int index) {
        this.model.remove(index);
        this.notifyWithListSize();
    }

    protected void notifyWithListSize() {
        this.setChanged();
        this.notifyObservers(model.size());
    }

    public DefaultListModel<E> getModel() {
        return this.model;
    }

    public int getSize() {
        return this.model.getSize();
    }
}

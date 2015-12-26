package at.ac.tuwien.shacl.plugin.events;

import java.util.Observable;
import java.util.Observer;

/**
 * Notifier for passing error events. Implementing the observable part of the observer pattern.
 */
public class ErrorNotifier extends Observable {
    private static ErrorNotifier notifier = new ErrorNotifier();

    public static void notify(String message) {
        notifier.setChanged();
        notifier.notifyObservers(message);
    }

    public static void register(Observer observer) {
        notifier.addObserver(observer);
    }

    public static void unregister(Observer observer) {
        notifier.deleteObserver(observer);
    }
}

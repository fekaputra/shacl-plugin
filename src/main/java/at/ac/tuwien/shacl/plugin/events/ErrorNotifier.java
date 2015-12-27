package at.ac.tuwien.shacl.plugin.events;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

/**
 * Notifier for passing error events. Implementing the observable part of the observer pattern.
 */
public class ErrorNotifier extends Observable {
    private static ErrorNotifier notifier = new ErrorNotifier();
    private static final Logger log = Logger.getLogger(ErrorNotifier.class);


    public static void notify(String message) {
        log.error(message);

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

package at.ac.tuwien.shacl.plugin.events;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by xlin on 25.12.2015.
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

package at.ac.tuwien.shacl.plugin.events;

import java.util.Observer;

/**
 * Wrapper wrapping ShaclValidation into one instance.
 * <p>
 * TODO eventually implement mediator pattern, so we can add multiple language validators.
 */
public class ShaclValidationRegistry {
    private static ShaclValidation validation = new ShaclValidation();

    public static void addObserver(Observer observer) {
        validation.addObserver(observer);
    }

    public static void removeObserver(Observer observer) {
        validation.deleteObserver(observer);
    }

    public static ShaclValidation getValidator() {
        return validation;
    }
}

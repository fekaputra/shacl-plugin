package at.ac.tuwien.shacl.plugin.util;

import java.util.*;

/**
 * Checks objects for types.
 */
public class TypeChecker {
    public static <T> Iterable<T> safe(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }
}

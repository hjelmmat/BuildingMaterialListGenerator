package main.Models.Buildable;

import java.util.Vector;

/**
 * Interface to be used by classes outside Buildable using only built-in types.
 */
public interface Buildable {
    Vector<Vector<String>> material();
}

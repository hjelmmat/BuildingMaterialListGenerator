package Models.Buildable;

import java.util.Vector;

/**
 * Interface to be used by classes outside Buildable using only built-in types.
 */
public interface Buildable {
    /**
     *
     * @return A Vector such that the first element describes the elements of the other vectors. The rest of the
     * elements are Vectors of type Material and the Quantity.
     */
    Vector<Vector<String>> materials(); // TODO: Make a hashset of strings with the categories "Titles" "Data"

    /**
     *
     * @return A Vector of instructions such that the first element is a Vector of instructions for Graphics.drawLine
     * and the second element is a Vector of instructions for Graphics.drawRectangle
     */
    Vector<Vector<Vector<Integer>>> drawingInstructions(); // TODO: Make a hashset of strings with the drawing type
}

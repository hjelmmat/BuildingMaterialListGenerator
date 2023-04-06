package models.buildable

import java.util.*

/**
 * Interface to be used by classes outside Buildable using only built-in types.
 */
interface Buildable {
    /**
     *
     * @return A Vector such that the first element describes the elements of the other vectors. The rest of the
     * elements are Vectors of type Material and the Quantity.
     */
    fun materials(): Vector<Vector<String>> // TODO: Make a hashset of strings with the categories "Titles" "Data"
}
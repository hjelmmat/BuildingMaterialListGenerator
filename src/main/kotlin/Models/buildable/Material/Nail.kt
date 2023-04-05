package models.buildable.material

/**
 * Nails used as material
 */
enum class Nail(private val niceString: String) : Material {
    TEN_D("10d");

    /**
     *
     * @return a nice String representation of the Nail
     */
    override fun toString(): String {
        return "$niceString nails"
    }
}
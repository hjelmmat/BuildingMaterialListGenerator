package models.buildable.material

import models.Measurement
import models.Measurement.Fraction

/**
 * Base class for all Lumber. This class assumes only lumber that can be bought from a store.
 * @param length - Length of
 * @param dimension - dimension of Lumber
 */
class Lumber(length: Measurement, private val dimension: Dimension) : Material {
    private val factoryMeasurement = FactoryLength.fromLength(length)

    /**
     *
     * @return a nice String representation of the Lumber
     */
    override fun toString(): String {
        return "$factoryMeasurement $dimension"
    }

    /**
     * Allow for the comparison of equality of two pieces of Lumber
     * @param other - the object to be compared
     * @return Indication if the two pieces of lumber are equal
     */
    override fun equals(other: Any?) = (other === this)
            || other is Lumber && factoryMeasurement == other.factoryMeasurement && dimension == other.dimension

    /**
     * @return An appropriate Hashcode by converting the dimension and length to Strings and using String.hashCode()
     */
    override fun hashCode(): Int {
        return ("$dimension$factoryMeasurement${this.javaClass.name}").hashCode()
    }

    /**
     * Enum to represent acceptable Lumber types
     */
    enum class Dimension(
        @JvmField val width: Measurement,
        @JvmField val height: Measurement,
        private val niceString: String
    ) {
        TWO_BY_FOUR(
            Measurement(1, Fraction.ONE_HALF), Measurement(3, Fraction.ONE_HALF),
            "2x4",
        ),
        TWO_BY_SIX(
            Measurement(1, Fraction.ONE_HALF), Measurement(5, Fraction.ONE_HALF),
            "2x6",
        );

        /**
         *
         * @return a nice String representation of the Dimension
         */
        override fun toString(): String {
            return niceString
        }
    }

    /**
     * Enum to represent Lumber that can be bought from a store. Will round general measurements to the shortest length
     * that is longer than the provided length.
     */
    enum class FactoryLength(private val length: Measurement) {
        TWO_FT(Measurement(24)), FOUR_FT(Measurement(48)), EIGHT_FT_PRECUT(
            Measurement(
                92,
                Fraction.FIVE_EIGHTH
            )
        ),
        EIGHT_FT(Measurement(96)), NINE_FT_PRECUT(
            Measurement(
                104,
                Fraction.FIVE_EIGHTH
            )
        ),
        TEN_FT(Measurement(120)), TWELVE_FT(Measurement(144)), FOURTEEN_FT(Measurement(168)), SIXTEEN_FT(Measurement(192)), TWENTY_FT(
            Measurement(240)
        );

        /**
         *
         * @return a nice String representation of the FactoryLength
         */
        override fun toString(): String {
            return length.toString()
        }

        companion object {
            @Throws(IllegalArgumentException::class)
            fun fromLength(length: Measurement): FactoryLength {
                for (factoryLength in values()) {
                    if (length <= factoryLength.length) {
                        return factoryLength
                    }
                }
                // If this part of the code is reached, then no valid length was found.
                throw IllegalArgumentException("A length cannot be greater than ${TWENTY_FT.length}, was $length")
            }
        }
    }
}
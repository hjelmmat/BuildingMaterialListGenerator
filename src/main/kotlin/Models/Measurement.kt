package models


/**
 * Contains positive distances limited to multiples of 1/16.
 */
class Measurement : Comparable<Measurement?> {
    private val integerValue: Int
    private val fractionValue: Fraction
    private val doubleValue get() = integerValue + fractionValue.value

    // TODO: Update numberOfPixels to be 1p = 1/16", let the graphic determine the scale
    val numberOfPixels get() = (this.doubleValue * 8).toInt()

    /**
     * @param integerValue - The integer portion of a measurement, for example 1"
     * @param fractionValue - The fraction portion of a measurement, for example 1/16"
     * @exception InvalidMeasurementException - Thrown when the measurement is less than 0" as negative numbers
     *  are not valid measurements.
     */
    constructor(integerValue: Int, fractionValue: Fraction = Fraction.ZERO) {
        if (integerValue < 0) {
            throw InvalidMeasurementException("measurement cannot be less than 0, was $integerValue")
        }
        this.integerValue = integerValue
        this.fractionValue = fractionValue
    }

    private constructor(doubleValue: Double) {
        if (doubleValue < 0) {
            throw InvalidMeasurementException("measurement cannot be less than 0, was -${Measurement(-doubleValue)}")
        }
        this.integerValue = doubleValue.toInt()
        this.fractionValue = Fraction.fromDouble(doubleValue)
    }

    /**
     * Created to not Conflict with the IllegalArgumentException that can throw with Integer.parseInt() used elsewhere
     */
    class InvalidMeasurementException(s: String) : IllegalArgumentException(s)

    /**
     * Allows for the comparison of the size of two Measurements according to the Comparable Interface
     * @param other - the object to be compared.
     * @return A positive, negative, or 0 int based on if o is larger, smaller, or the same size.
     */
    override fun compareTo(other: Measurement?): Int {
        return this.doubleValue.compareTo(other!!.doubleValue)
    }

    /**
     * Allow for the comparison of equality of two Measurements
     * @param other - the object to be compared
     * @return Indication if the two Measurements are equal
     */
    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is Measurement && this.doubleValue == other.doubleValue)
    }

    /**
     * @return An appropriate hash value is to move the decimal places enough to convert the double into an int. Since
     * the smallest decimal number is .0625 (1/16), multiplying the double value by 10,000 will ensure only 0s are after
     * the decimal and can be safely chopped off as an int.
     */
    override fun hashCode(): Int {
        return (this.doubleValue * 10000.0).toInt()
    }

    /**
     *
     * @param multiplicand - The value to multiply this Measurement by.
     * @return A Measurement of size multiplied by the multiplicand.
     */
    fun multiply(multiplicand: Int) = Measurement(this.doubleValue * multiplicand)

    /**
     *
     * @param divisor - The Measurement to divide this Measurement by.
     * @return - The number of times this Measurement can be divided by the divisor.
     */
    fun divide(divisor: Measurement) = this.doubleValue / divisor.doubleValue

    /**
     *
     * @param subtrahend - The value to subtract from this Measurement
     * @return A Measurement that is the difference between this and the subtrahend
     */
    fun subtract(subtrahend: Measurement) = Measurement(this.doubleValue - subtrahend.doubleValue)

    /**
     *
     * @param addend - Measurement to add to this Measurement
     * @return Resultant Measurement of addition
     */
    fun add(addend: Measurement) = Measurement(this.doubleValue + addend.doubleValue)

    /**
     *
     * @return The Measurement as a string in the format of: int-fraction"
     */
    override fun toString(): String {
        var result = ""
        val fractionString = fractionValue.toString()
        if (integerValue == 0) {
            result += fractionString
        } else {
            result += integerValue
            if (fractionValue != Fraction.ZERO) {
                result += "-$fractionString"
            }
        }
        return result + "\"" // Add one double quote to denote inches.
    }

    /**
     * The Fraction enum is used to allow for non-int Measurements but restrict the options to commonly used values,
     * namely multiples of 1/16.
     */
    enum class Fraction(numerator: Int) {
        ZERO(0), ONE_SIXTEENTH(1), ONE_EIGHTH(2), THREE_SIXTEENTH(3),
        ONE_FOURTH(4), FIVE_SIXTEENTH(5), THREE_EIGHTH(6), SEVEN_SIXTEENTH(7),
        ONE_HALF(8), NINE_SIXTEENTH(9), FIVE_EIGHTH(10), ELEVEN_SIXTEENTH(11),
        THREE_FOURTH(12), THIRTEEN_SIXTEENTH(13), SEVEN_EIGHTH(14),
        FIFTEEN_SIXTEENTH(15);

        private val maximumFraction = 16.0
        val value = numerator / this.maximumFraction
        private val niceString: String

        init {
            niceString = if (numerator == 0) {
                "0"
            } else if (numerator % 8 == 0) { // Only the 1/2 case is represented here
                "1/2"
            } else if (numerator % 4 == 0) {
                "${numerator / 4}/4"
            } else if (numerator % 2 == 0) {
                "${numerator / 2}/8"
            } else {
                "$numerator/16"
            }
        }

        override fun toString(): String {
            return this.niceString
        }

        companion object {
            private val doubleMap: MutableMap<Double, Fraction> = HashMap()
            private val stringMap: MutableMap<String, Fraction> = HashMap()

            init {
                for (fraction in Fraction.values()) {
                    doubleMap[fraction.value] = fraction
                    stringMap[fraction.toString()] = fraction
                }
            }

            /**
             *
             * @param fractionValue - The double value to convert into an enum
             * @return The appropriate Fraction for the given value
             */
            fun fromDouble(fractionValue: Double): Fraction {
                return doubleMap[fractionValue % 1]!!
            }

            fun fromString(fractionString: String): Fraction {
                return stringMap[fractionString]!!
            }
        }

    }
}
package Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains positive distances limited to multiples of 1/16.
 */
public class Measurement implements Comparable<Measurement>{
    private int integer; // This should only be updated via this.updateIntegerValue
    private Fraction fraction;

    /**
     * Created to not Conflict with the IllegalArgumentException that can throw with Integer.parseInt() used elsewhere
     */
    public static class InvalidMeasurementException extends IllegalArgumentException {
        public InvalidMeasurementException(String s) {
            super(s);
        }
    }

    /**
     * The Fraction enum is used to allow for non-int Measurements but restrict the options to commonly used values,
     * namely multiples of 1/16.
     */
    public enum Fraction {
        ZERO(0), ONE_SIXTEENTH(1), ONE_EIGHTH(2), THREE_SIXTEENTH(3), ONE_FOURTH(4), FIVE_SIXTEENTH(5), THREE_EIGHTH(6),
        SEVEN_SIXTEENTH(7), ONE_HALF(8), NINE_SIXTEENTH(9), FIVE_EIGHTH(10), ELEVEN_SIXTEENTH(11), THREE_FOURTH(12),
        THIRTEEN_SIXTEENTH(13), SEVEN_EIGHTH(14), FIFTEEN_SIXTEENTH(15);

        private final double value;
        private final String niceString;
        private static final double maximumFraction = 16;
        private static final Map<Double, Fraction> doubleMap = new HashMap<>();
        private static final Map<String, Fraction> stringMap = new HashMap<>();

        Fraction(int numerator) {
            this.value = numerator / maximumFraction;
            if (numerator == 0 ) {
                this.niceString = "0";
            }
            else if (numerator % 8 == 0) { // Only the 1/2 case is represented here
                this.niceString = "1/2";
            }
            else if (numerator % 4 == 0) {
                int reducedNumerator = numerator / 4;
                this.niceString = reducedNumerator + "/4";
            }
            else if (numerator % 2 == 0) {
                int reducedNumerator = numerator / 2;
                this.niceString = reducedNumerator + "/8";
            }
            else {
                this.niceString = numerator + "/16";
            }
        }

        static {
            for (Fraction fraction : Fraction.values()) {
                doubleMap.put(fraction.value, fraction);
                stringMap.put(fraction.toString(), fraction);
            }
        }

        @Override
        public String toString() {
            return this.niceString;
        }

        /**
         *
         * @param fractionValue - The double value to convert into an enum
         * @return The appropriate Fraction for the given value
         */
        static Fraction valueOf(double fractionValue) {
            return doubleMap.get(fractionValue);
        }

        static Fraction fromString(String fractionString) { return stringMap.get(fractionString); }
    }

    /**
     * Main Constructor for creating a Measurement meant to represent a building measurement, for example 1 and 1/16"
     * @param integerValue - The integer portion of a measurement, for example 1"
     * @param fractionValue - The fraction portion of a measurement, for example 1/16"
     * @exception InvalidMeasurementException - Thrown when the measurement is less than 0" as negative numbers
     * are not valid measurements.
     *
     */
    public Measurement(int integerValue, Fraction fractionValue) throws InvalidMeasurementException {
        this.updateIntegerValue(integerValue);
        this.fraction = fractionValue;
    }

    /**
     * Convenience Constructor for whole numbers.
     * @see Measurement constructor.
     *
     */
    public Measurement(int integerValue) throws InvalidMeasurementException {
        this(integerValue, Fraction.ZERO);
    }

    private void updateIntegerValue(int integerValue) {
        if (integerValue < 0) {
            String errorMessage = "measurement cannot be less than 0, was %d";
            throw new InvalidMeasurementException(String.format(errorMessage, integerValue));
        }
        else {
            this.integer = integerValue;
        }
    }

    private double doubleValue() { return this.integer + this.fraction.value; }

    /**
     * Allows for the comparison of the size of two Measurements according to the Comparable Interface
     * @param o - the object to be compared.
     * @return A positive, negative, or 0 int based on if o is larger, smaller, or the same size.
     */
    @Override
    public int compareTo(Measurement o) {
        double thisSum = this.doubleValue();
        double oSum = o.doubleValue();
        return Double.compare(thisSum, oSum);
    }

    public Measurement greaterValue(Measurement o) {
        return this.compareTo(o) > 0 ? this : o;
    }

    /**
     * Allow for the comparison of equality of two Measurements
     * @param obj - the object to be compared
     * @return Indication if the two Measurements are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Measurement m)) {
            return false;
        }
        return this.doubleValue() == m.doubleValue();
    }

    /**
     * @return An appropriate hash value is to move the decimal places enough to convert the double into an int. Since
     * the smallest decimal number is .0625 (1/16), multiplying the double value by 10,000 will ensure only 0s are after
     * the decimal and can be safely chopped off as an int.
     */
    @Override
    public int hashCode() {
        return (int) (this.doubleValue() * 10000.0);
    }

    private Measurement updateMeasurementFromDouble(double newMeasurement) {
        int wholeNumber = (int) Math.floor(newMeasurement);
        Fraction remainder = Fraction.valueOf(newMeasurement % 1);
        this.updateIntegerValue(wholeNumber);
        this.fraction = remainder;
        return this;
    }

    /**
     *
     * @param multiplicand - The value to multiply this Measurement by.
     * @return A Measurement of size multiplied by the multiplicand.
     */
    public Measurement multiply(int multiplicand) {
        return this.updateMeasurementFromDouble(this.doubleValue() * multiplicand);
    }

    /**
     *
     * @param divisor - The Measurement to divide this Measurement by.
     * @return - The number of times this Measurement can be divided by the divisor.
     */
    public double divide(Measurement divisor) {
        return this.doubleValue() / divisor.doubleValue();
    }

    /**
     *
     * @param subtrahend - The value to subtract from this Measurement
     * @return A Measurement that is the difference between this and the subtrahend
     */
    public Measurement subtract(Measurement subtrahend) throws InvalidMeasurementException {
        return this.updateMeasurementFromDouble(this.doubleValue() - subtrahend.doubleValue());
    }

    /**
     *
     * @param addend - Measurement to add to this Measurement
     * @return Resultant Measurement of addition
     */
    public Measurement add(Measurement addend) {
        return this.updateMeasurementFromDouble(this.doubleValue() + addend.doubleValue());
    }

    /**
     *
     * @return The Measurement as a string in the format of: int-fraction"
     */
    public String toString() {
        String result = "";
        String fractionString = this.fraction.niceString;
        if (this.integer == 0) {
            result += fractionString;
        }
        else {
            result += this.integer;
            if (!this.fraction.equals(Fraction.ZERO)) {
                result += "-" + fractionString;
            }
        }
        return result  + "\""; // Add one double quote to denote inches.
    }

    /**
     *
     * @return a new Measurement that is a clone of this one.
     */
    @Override
    public Measurement clone() {
        return new Measurement(this.integer, this.fraction);
    }

    /**
     *
     * @return - The number of pixels it takes to draw this measurement.
     */
    public Integer numberOfPixels() {
        return (int) (this.doubleValue() * 8);
    }
}

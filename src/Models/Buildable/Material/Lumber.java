package Models.Buildable.Material;

import Models.Measurement;

/**
 * Base class for all Lumber. This class assumes only lumber that can be bought from a store.
 */
public class Lumber implements Material {
    private final FactoryLength factoryMeasurement;
    private final Dimension dimension;

    /**
     * Enum to represent acceptable Lumber types
     */
    public enum Dimension {
        TWO_BY_FOUR(new Measurement(1, Measurement.Fraction.ONE_HALF), new Measurement(3, Measurement.Fraction.ONE_HALF),
                "2x4");

        public final Measurement width;
        public final Measurement height;
        private final String niceString;

        Dimension(Measurement width, Measurement height, String niceString) {
            this.width = width;
            this.height = height;
            this.niceString = niceString;
        }

        /**
         *
         * @return a nice String representation of the Dimension
         */
        @Override
        public String toString() {
            return this.niceString;
        }
    }

    /**
     * Enum to represent Lumber that can be bought from a store. Will round general measurements to the shortest length
     * that is longer than the provided length.
     */
    public enum FactoryLength {
        TWO_FT(new Measurement(24)), FOUR_FT(new Measurement(48)), EIGHT_FT_PRECUT(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH)),
        EIGHT_FT(new Measurement(96)), NINE_FT_PRECUT(new Measurement(104, Measurement.Fraction.FIVE_EIGHTH)),
        TEN_FT(new Measurement(120)), TWELVE_FT(new Measurement(144)), FOURTEEN_FT(new Measurement(168)),
        SIXTEEN_FT(new Measurement(192)), TWENTY_FT(new Measurement(240));

        private final Measurement length;

        FactoryLength(Measurement length) {
            this.length = length;
        }

        private static FactoryLength fromLength(Measurement length) throws IllegalArgumentException {
            for (FactoryLength factoryLength : FactoryLength.values()) {
                if (length.compareTo(factoryLength.length) <= 0) {
                    return factoryLength;
                }
            }
            // If this part of the code is reached, then no valid length was found.
            String message = "A length cannot be greater than %s, was %s";
            throw new IllegalArgumentException(String.format(message,
                    FactoryLength.TWENTY_FT.length.toString(),
                    length.toString()));
        }

        /**
         *
         * @return a nice String representation of the FactoryLength
         */
        @Override
        public String toString() {
            return this.length.toString();
        }
    }

    /**
     *
     * @param length - Length of
     * @param dimension - dimension of Lumber
     */
    public Lumber(Measurement length, Dimension dimension) throws IllegalArgumentException {
        this.factoryMeasurement = FactoryLength.fromLength(length);
        this.dimension = dimension;
    }

    /**
     *
     * @return a nice String representation of the Lumber
     */
    @Override
    public String toString() {
        return this.factoryMeasurement.toString() + " " + this.dimension.toString();
    }

    /**
     * Allow for the comparison of equality of two pieces of Lumber
     * @param obj - the object to be compared
     * @return Indication if the two pieces of lumber are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Lumber l)) {
            return false;
        }
        return this.factoryMeasurement.equals(l.factoryMeasurement) && this.dimension == l.dimension;
    }

    /**
     * @return An appropriate Hashcode by converting the dimension and length to Strings and using String.hashCode()
     */
    @Override
    public int hashCode() {
        return (this.dimension.toString() + this.factoryMeasurement.toString() + this.getClass().getName()).hashCode();
    }
}

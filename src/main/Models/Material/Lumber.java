package main.Models.Material;

import main.Models.Installable;
import main.Models.Measurement;

/**
 * Base class for all Lumber. This class assumes only lumber that can be bought from a store.
 */
public class Lumber implements Installable, Material {
    public final FactoryLength factoryMeasurement;
    public final Dimension dimension;
    protected final MaterialList material;

    /**
     * Enum to represent acceptable Lumber types
     */
    public enum Dimension {
        TWO_BY_FOUR(new Measurement(1, Measurement.Fraction.ONE_HALF), new Measurement(3, Measurement.Fraction.ONE_HALF));

        public final Measurement width;
        public final Measurement height;

        Dimension(Measurement width, Measurement height) {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Enum to represent Lumber that can be bought from a store. Will round general measurements to the shortest length
     * that is longer than the provided length.
     */
    public enum FactoryLength {
        TWO_FT(new Measurement(24)), FOUR_FT(new Measurement(48)), EIGHT_FT_WALL(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH)),
        EIGHT_FT(new Measurement(96)), NINE_FT_WALL(new Measurement(104, Measurement.Fraction.FIVE_EIGHTH)),
        TEN_FT(new Measurement(120)), TWELVE_FT(new Measurement(144)), FOURTEEN_FT(new Measurement(168)),
        SIXTEEN_FT(new Measurement(192)), TWENTY_FT(new Measurement(240));

        public final Measurement length;

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
            String message = "A FactoryLength cannot be greater than %s, was actually %s";
            throw new IllegalArgumentException(String.format(message,
                    FactoryLength.TWENTY_FT.length.asString(),
                    length.asString()));
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

        this.material = new MaterialList();
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
        return (this.dimension + this.factoryMeasurement.length.asString() + this.getClass().getName()).hashCode();
    }

    /**
     *
     * @return The Material required for this Lumber
     */
    @Override
    public MaterialList material() {
        return this.material.isEmpty() ? this.material.addMaterial(this, 1) : this.material;
    }
}

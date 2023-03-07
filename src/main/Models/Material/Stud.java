package main.Models.Material;

import main.Models.Measurement;

/**
 * Class used to describe a stud, essentially a c-style struct
 */
public class Stud extends Lumber {
    public final Measurement installedLength;
    private final static Nails nailType = Nails.TEN_D;
    private final static int numberOfNails = 6;

    /**
     *
     * @param length - The length of the stud
     * @param dimension - the width/height dimension of the stud.
     */
    public Stud(Measurement length, Dimension dimension) {
        super(length, dimension);
        this.installedLength = length;
    }

    /**
     * Default Constructor for a Stud assuming 8' 2x4 walls
     */
    public Stud() {
        this(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), Dimension.TWO_BY_FOUR);
    }

    /**
     * Studs are only equal if the installedLength is the same, which will also have the same FactoryLength for the
     * MaterialList
     * @param obj - the object to be compared
     * @return Indication if the two studs are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Stud s)) {
            return false;
        }
        return super.equals(s) && this.installedLength.equals(s.installedLength);
    }

    /**
     *
     * @return hash code based on the dimension, installed length, and class name. Class name is necessary to be
     * different from other classes with dimension and installation length.
     */
    @Override
    public int hashCode() {
        return  (this.dimension + this.installedLength.asString() + this.getClass().getName()).hashCode();
    }

    /**
     *
     * @return A MaterialList of the material required to install the stud.
     */
    @Override
    public MaterialList material() {
        return this.material.isEmpty()
                ? this.material.addMaterials(new Lumber(this.installedLength, this.dimension).material()).addMaterial(nailType, numberOfNails)
                : this.material;
    }
}

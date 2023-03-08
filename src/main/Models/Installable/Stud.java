package main.Models.Installable;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Nails;
import main.Models.Measurement;

/**
 * Class used to describe a stud, essentially a c-style struct
 */
public class Stud implements Installable {
    public final Measurement installedLength;
    public final Lumber.Dimension dimension;
    private final MaterialList material;
    private final static Nails nailType = Nails.TEN_D;
    private final static int numberOfNails = 6;

    /**
     *
     * @param length - The length of the stud
     * @param dimension - the width/height dimension of the stud.
     */
    public Stud(Measurement length, Lumber.Dimension dimension) {
        this.installedLength = length;
        this.dimension = dimension;
        this.material = new MaterialList().addMaterial(new Lumber(this.installedLength, this.dimension), 1)
                .addMaterial(nailType, numberOfNails);
    }

    /**
     * Default Constructor for a Stud assuming 8' 2x4 walls
     */
    public Stud() {
        this(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), Lumber.Dimension.TWO_BY_FOUR);
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
        return this.installedLength.equals(s.installedLength) && this.dimension.equals(s.dimension);
    }

    /**
     *
     * @return hash code based on the dimension, installed length, and class name. Class name is necessary to be
     * different from other classes with dimension and installation length.
     */
    @Override
    public int hashCode() {
        return  (this.dimension + this.installedLength.toString() + this.getClass().getName()).hashCode();
    }

    /**
     *
     * @return A MaterialList of the material required to install the stud.
     */
    @Override
    public MaterialList material() {
        return this.material;
    }
}

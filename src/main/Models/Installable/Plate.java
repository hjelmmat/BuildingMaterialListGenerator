package main.Models.Installable;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Nails;
import main.Models.Measurement;

public class Plate implements Installable{
    public final Measurement installedLength;
    public final Lumber.Dimension dimension;
    private final MaterialList material;
    private final static Measurement nailSpacing = new Measurement(12);
    private final static Nails nailType = Nails.TEN_D;

    /**
     * @param length    - Length of plate
     * @param dimension - dimension of Lumber to use for plate
     */
    public Plate(Measurement length, Lumber.Dimension dimension) throws IllegalArgumentException {
        this.installedLength = length;
        this.dimension = dimension;
        Lumber lumber = new Lumber(installedLength, dimension);
        this.material = new MaterialList().addMaterial(lumber, 1).addMaterial(nailType, this.numberOfNails());
    }

    private int numberOfNails() {
        // Plates should be installed with a pair of nails every 12" with a set at the beginning and the end.
        int numberOfNailPairs = (int) Math.ceil(installedLength.divide(nailSpacing)) + 1;
        return numberOfNailPairs * 2;
    }

    /**
     * Allow for the comparison of equality of two plates
     * @param obj - the object to be compared
     * @return Indication if the two plates are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Plate p)) {
            return false;
        }
        return this.installedLength.equals(p.installedLength) && this.dimension.equals(p.dimension);
    }

    /**
     * @return An appropriate Hashcode by converting the dimension and length to Strings and using String.hashCode()
     */
    @Override
    public int hashCode() {
        return  (this.dimension + this.installedLength.asString() + this.getClass().getName()).hashCode();
    }

    /**
     *
     * @return A MaterialList of the material required to install the Plate.
     */
    @Override
    public MaterialList material() {
        return this.material;
    }
}

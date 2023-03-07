package main.Models;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Nails;

public class Plate extends Lumber {
    public final Measurement installedLength;
    private final static Measurement nailSpacing = new Measurement(12);
    private final static Nails nailType = Nails.TEN_D;

    /**
     * @param length    - Length of plate
     * @param dimension - dimension of Lumber to use for plate
     */
    public Plate(Measurement length, Dimension dimension) throws IllegalArgumentException {
        super(length, dimension);
        this.installedLength = length;
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
        return super.equals(p) && this.installedLength.equals(p.installedLength);
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
        if (this.material.isEmpty()) {
            this.material.addMaterial(new Lumber(this.installedLength, this.dimension), 1);

            // Plates should be installed with a pair of nails every 12" with a set at the beginning and the end.
            int numberOfNailPairs = (int) Math.ceil(installedLength.divide(nailSpacing)) + 1;
            this.material.addMaterial(nailType, numberOfNailPairs * 2);
        }
        return this.material;
    }
}

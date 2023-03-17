package Models.Buildable.Installable;

import Graphics.Drawable;
import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;

public class Plate implements Installable, Drawable {
    private final static Measurement nailSpacing = new Measurement(12);
    private final static Nail nailType = Nail.TEN_D;

    private final Measurement installedLength;
    private final Lumber.Dimension dimension;
    private final MaterialList material;

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
        return  (this.dimension + this.installedLength.toString() + this.getClass().getName()).hashCode();
    }

    /**
     *
     * @return A MaterialList of the material required to install the Plate.
     */
    @Override
    public MaterialList materialList() {
        return this.material;
    }

    /**
     *
     * @return GraphicsList showing where the plate should be drawn with both a Rectangle and a Line
     */
    @Override
    public GraphicsList drawingInstructions() {
        Measurement zero = new Measurement(0);
        Measurement width = this.dimension.width;
        return new GraphicsList().addGraphic(new RectangleInstructions(zero, zero, this.installedLength, width));
    }
}

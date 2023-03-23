package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Measurement;

/**
 * A Stud that is installed horizontally is a plate. Typically installed with nails at the start and end of the plate
 * as well as every 12 inches.
 */
public class Plate extends Stud {
    /**
     * @param length    - Length of plate
     * @param dimension - dimension of Lumber to use for plate
     */
    public Plate(Measurement length, Lumber.Dimension dimension) throws IllegalArgumentException {
        super(length, dimension);
    }

    @Override
    int numberOfNails() {
        // Plates should be installed with a pair of nails every 12" with a set at the beginning and the end.
        return Plate.numberOfNails(this.totalWidth());
    }

    /**
     * Other studs may be attached like plates without being plates themselves. This allows for use of the same
     * calculation.
     * @param length - Length of the plate to find nails for
     * @return The number of nails to use
     */
    static int numberOfNails(Measurement length) {
        Measurement nailSpacing = new Measurement(12);
        int numberOfNailPairs = (int) Math.ceil(length.divide(nailSpacing)) + 1;
        return numberOfNailPairs * 2;
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Plate
     */
    @Override
    public Measurement totalWidth() {
        return super.totalHeight();
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Plate
     */
    @Override
    public Measurement totalHeight() {
        return super.totalWidth();
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Plate (as a
     * sideways {@link Stud})
     */
    @Override
    public GraphicsList graphicsList() {
        Measurement zero = new Measurement(0);
        return new GraphicsList().addGraphic(new RectangleInstructions(zero, zero, this.totalWidth(), this.totalHeight()));
    }
}

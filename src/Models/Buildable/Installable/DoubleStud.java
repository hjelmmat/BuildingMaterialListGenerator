package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Measurement;

/**
 * Frequently, studs are place right next to each other and that requires them to be nailed together. This class ensures
 * the material list and drawing instructions are updated accordingly.
 */
public class DoubleStud extends Stud {
    private static final Measurement nailSpacing = new Measurement(12);

    /**
     *
     * @param length - Length of Double stud to create
     * @param dimension - Dimension of one of the studs to create the doubleStud
     */
    public DoubleStud(Measurement length, Lumber.Dimension dimension) {
        super(length, dimension);
        int numberOfNails = (int) (Math.ceil(length.divide(nailSpacing)) + 1) * 2;
        this.material.addMaterial(Stud.nailType, numberOfNails);
        this.material.addMaterial(new Lumber(length, dimension), 1);
    }

    /**
     * A Convenience constructor for making double studs from a stud
     * @param stud - Stud to create a double stud from
     */
    public DoubleStud(Stud stud) {
        this(stud.installedLength, stud.dimension);
    }

    /**
     *
     * @return a GraphicsList of the instructions to draw this DoubleStud
     */
    @Override
    public GraphicsList drawingInstructions() {
        GraphicsList result = super.drawingInstructions();
        Measurement zero = new Measurement(0);
        return result.addGraphic(new RectangleInstructions(zero, zero, this.dimension.width.clone().multiply(2), this.installedLength));
    }
}

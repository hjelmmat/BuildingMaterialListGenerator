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
    /**
     *
     * @param length - Length of Double stud to create
     * @param dimension - Dimension of one of the studs to create the doubleStud
     */
    public DoubleStud(Measurement length, Lumber.Dimension dimension) {
        super(length, dimension);

        // The second stud is attached like a plate, so use the static method here.
        this.material.addMaterial(Stud.nailType, Plate.numberOfNails(length));
        this.material.addMaterial(new Lumber(length, dimension), 1);
    }

    /**
     * A Convenience constructor for making double studs from a stud
     * @param stud - Stud to create a double stud from
     */
    public DoubleStud(Stud stud) {
        this(stud.installedHeight, stud.dimension);
    }


    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this DoubleStud
     */
    @Override
    public Measurement totalWidth() {
        return this.dimension.width.clone().multiply(2);
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this DoubleStud
     */
    @Override
    public GraphicsList graphicsList() {
        GraphicsList result = super.graphicsList();
        Measurement zero = new Measurement(0);
        return result.addGraphic(new RectangleInstructions(this.dimension.width, zero, this.dimension.width, this.installedHeight));
    }
}

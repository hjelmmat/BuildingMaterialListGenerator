package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;

/**
 * Class used to describe a stud, essentially a c-style struct
 */
public class Stud implements Installable {
    final Measurement installedHeight;
    final Lumber.Dimension dimension;
    final MaterialList material;
    final static Nail nailType = Nail.TEN_D;

    /**
     *
     * @param height - The height of the stud
     * @param dimension - the width/height dimension of the stud.
     */
    public Stud(Measurement height, Lumber.Dimension dimension) throws IllegalArgumentException {
        this.installedHeight = height;
        this.dimension = dimension;
        this.material = new MaterialList().addMaterial(new Lumber(this.installedHeight, this.dimension), 1)
                .addMaterial(nailType, this.numberOfNails());
    }

    /**
     * Default Constructor for a Stud assuming 8' 2x4 walls
     */
    public Stud() throws IllegalArgumentException {
        this(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), Lumber.Dimension.TWO_BY_FOUR);
    }

    int numberOfNails(){
        return 6;
    }

    /**
     * Studs are only equal if the installedHeight is the same, which will also have the same FactoryLength for the
     * MaterialList
     * @param obj - the object to be compared
     * @return Indication if the two studs are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (!(obj instanceof Stud s)) { return false; }

        // subclasses typically change the materialList so to ensure equality matters with subclasses, we verify
        // the difference
        return this.hashCode() == s.hashCode();
    }

    /**
     *
     * @return hash code based on the dimension, installed length, and class name. Class name is necessary to be
     * different from other classes with dimension and installation length.
     */
    @Override
    public int hashCode() {
        return  (this.dimension.toString() + this.installedHeight.toString() + this.getClass().getName()).hashCode();
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Stud
     */
    @Override
    public Measurement totalWidth() {
        return this.dimension.width.clone();
    }

    /**
     *
     * @return - A {@link MaterialList} of {@link Models.Buildable.Material.Material} used to create this Stud
     */
    @Override
    public MaterialList materialList() {
        return this.material;
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Stud
     */
    @Override
    public GraphicsList graphicsList() {
        Measurement zero = new Measurement(0);
        return new GraphicsList().addGraphic(new RectangleInstructions(zero, zero, this.dimension.width, this.installedHeight));
    }
}

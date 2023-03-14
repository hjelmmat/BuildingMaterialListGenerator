package Models.Buildable;

import Models.Buildable.Installable.Installable;
import Models.Buildable.Installable.Layout;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Installable.Plate;
import Models.Buildable.Installable.Stud;
import Models.Measurement;

import java.util.Vector;

/**
 * Class used to describe what it takes to build a wall
 */
public class Wall implements Buildable, Installable {
    private final Stud stud;
    private final static boolean loadBearing = true;
    private final Layout layout;
    private final MaterialList material;

    private static final Lumber.Dimension studType = Lumber.Dimension.TWO_BY_FOUR;
    private static final Measurement studSeparation = new Measurement(16);
    private static final int minimumNumberOfStuds = 2;

    /**
     *
     * @param length - Length of wall to create
     * @param height - Height of wall to create
     * @throws IllegalArgumentException - Thrown when the length or height is less than the minimum wall length/height
     */
    Wall(Measurement length, Measurement height) throws IllegalArgumentException {
        // All walls have at least one top plate, but the nails to attach it are from the studs
        this.material = new MaterialList().addMaterial(new Lumber(length, studType), 1);

        // add the top and bottom plates
        Plate plate = new Plate(length, studType);
        this.material.addMaterials(plate.materialList());
        if (loadBearing) {
            this.material.addMaterials(plate.materialList());
        }
        int numberOfPlates = loadBearing ? 3 : 2;

        // The height of a wall includes the top and bottom plates and the studs, so we need to remove the height of the
        // plates to get the heights of the studs
        Measurement heightOfAllPlates = studType.width.clone().multiply(numberOfPlates);
        this.stud = new Stud(validateParameter(height, heightOfAllPlates, "height").clone().subtract(heightOfAllPlates),
                studType);
        this.layout = this.createLayout(validateParameter(length, studType.width.clone().multiply(minimumNumberOfStuds),
                "length"));
        this.material.addMaterials(this.layout.materialList());
    }

    /**
     * A convenience constructor when creating a standard 8', load bearing wall.
     * @param length - Length of wall to create
     * @throws IllegalArgumentException - Thrown when the length is less than the minimum wall length/height
     */
    Wall(Measurement length) throws IllegalArgumentException {
        this(length, new Measurement(97, Measurement.Fraction.ONE_EIGHTH));
    }

    private static Measurement validateParameter(Measurement parameter, Measurement minimumValue, String type)
            throws IllegalArgumentException {
        String exceptionMessageBase = "%s cannot be less than %s, was %s";
        if (parameter.compareTo(minimumValue) < 0) {
            String lengthExceptionMessage = String.format(exceptionMessageBase, type, minimumValue, parameter);
            throw new IllegalArgumentException(lengthExceptionMessage);
        }
        return parameter;
    }

    private Layout createLayout(Measurement length) {
        Layout currentLayout = new Layout();
        Measurement currentPosition = new Measurement(0);

        // add the initial stud
        currentLayout.addStudAt(currentPosition, this.stud);
        currentPosition.add(studSeparation);

        // add the rest of the studs. There needs to be enough room for both the full separation and the width of a stud
        Measurement lastPosition = length.clone().subtract(studType.width);
        while (currentPosition.compareTo(lastPosition) < 0) {
            currentLayout.addStudAt(currentPosition, this.stud);
            currentPosition.add(studSeparation);
        }

        // add the final stud
        currentLayout.addStudAt(lastPosition, this.stud);
        return currentLayout;
    }

    /**
     *
     * @return The studs required to create this wall
     */
    Layout layout() {
        return this.layout;
    }

    /**
     *
     * @return The material required to build this wall
     */
    @Override
    public Vector<Vector<String>> materials() {
        return this.material.materials();
    }

    /**
     *
     * @return The MaterialList of the material required to build this wall
     */
    @Override
    public MaterialList materialList() {
        return this.material;
    }
}

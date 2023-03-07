package main.Models.Installable;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Measurement;

/**
 * Class used to describe what it takes to build a wall
 */
public class Wall implements Installable {
    private final Stud stud;
    private final static boolean loadBearing = true;
    private final Layout layout;
    private final int numberOfStuds;
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
    public Wall(Measurement length, Measurement height) throws IllegalArgumentException {
        // All walls have at least one top plate, but the nails to attach it are from the studs
        this.material = new MaterialList().addMaterial(new Lumber(length, studType), 1);

        // add the top and bottom plates
        Plate plate = new Plate(length, studType);
        this.material.addMaterials(plate.material());
        if (loadBearing) {
            this.material.addMaterials(plate.material());
        }
        int numberOfPlates = loadBearing ? 3 : 2;

        Measurement heightOfAllPlates = studType.width.clone().multiply(numberOfPlates);
        this.stud = new Stud(validateParameter(height, heightOfAllPlates, "height").clone().subtract(heightOfAllPlates),
                studType);
        this.layout = this.createLayout(validateParameter(length, studType.width.clone().multiply(minimumNumberOfStuds),
                "length"));
        this.material.addMaterials(this.layout.material());
        this.numberOfStuds = this.layout.size();
    }

    /**
     * A convenience constructor when creating a standard 8', load bearing wall.
     * @param length - Length of wall to create
     * @throws IllegalArgumentException - Thrown when the length is less than the minimum wall length/height
     */
    public Wall(Measurement length) throws IllegalArgumentException {
        this(length, new Measurement(97, Measurement.Fraction.ONE_EIGHTH));
    }

    private static Measurement validateParameter(Measurement parameter, Measurement minimumValue, String type)
            throws IllegalArgumentException {
        String exceptionMessageBase = "%s cannot be less than %s; %s was %s";
        if (parameter.compareTo(minimumValue) < 0) {
            String lengthExceptionMessage = String.format(exceptionMessageBase, type, minimumValue.asString(), type, parameter.asString());
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
     * @return the number of studs required to build the wall
     */
    public int numberOfStuds() {
        return this.numberOfStuds;
    }

    /**
     *
     * @return The studs required to create this wall
     */
    public Layout layout() {
        return this.layout;
    }

    @Override
    public MaterialList material() {
        return this.material;
    }
}

package main.Models;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Stud;

/**
 * Class used to describe what it takes to build a wall
 */
public class Wall implements Installable {
    private final Stud stud;
    private final Lumber[] plates;
    private final static boolean loadBearing = true;
    private static final Stud.Dimension studType = Stud.Dimension.TWO_BY_FOUR;
    private static final Measurement studSeparation = new Measurement(16);
    private static final int minimumNumberOfStuds = 2;
    private final Layout layout;
    private final int numberOfStuds;

    /**
     *
     * @param length - Length of wall to create
     * @param height - Height of wall to create
     * @throws IllegalArgumentException - Thrown when the length or height is less than the minimum wall length/height
     */
    public Wall(Measurement length, Measurement height) throws IllegalArgumentException {
        // add the top and bottom plates
        this.plates = new Lumber[3];
        this.plates[0] = new Plate(length, studType);
        this.plates[1] = new Lumber(length, studType);
        if (loadBearing) {
            this.plates[2] = new Plate(length, studType);
        }

        Measurement heightOfAllPlates = studType.width.clone().multiply(this.plates.length);
        this.stud = new Stud(validateParameter(height, heightOfAllPlates, "height").clone().subtract(heightOfAllPlates),
                studType);
        this.layout = this.createLayout(validateParameter(length, studType.width.clone().multiply(minimumNumberOfStuds),
                "length"));
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
        MaterialList result = new MaterialList();

        // Add the plates first
        for (Lumber plate : this.plates) {
            result.addMaterials(plate.material());
        }

        // Add the material from the layout
        result.addMaterials(this.layout.material());

        return result;
    }
}

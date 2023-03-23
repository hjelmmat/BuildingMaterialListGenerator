package Models.Buildable;

import Graphics.GraphicsList;
import Models.Buildable.Installable.*;
import Models.Buildable.Installable.DoubleStud;
import Models.Buildable.Installable.Layout;
import Models.Buildable.Installable.Plate;
import Models.Buildable.Installable.Stud;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Measurement;

import java.util.TreeMap;
import java.util.Vector;

/**
 * Class used to describe what it takes to build a wall
 */
public class Wall implements Buildable, Installable {
    private final Stud stud;
    private final static boolean loadBearing = true;
    private final Layout layout;
    private final TreeMap<Measurement, Plate> platesHeightMap;
    private final MaterialList plateMaterials;
    private final Measurement studHeightShift;
    private final Measurement studHeight;
    private final Measurement length;

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
        this.length = length;
        // All walls have at least one top plate, but the nails to attach it are from the studs
        this.plateMaterials = new MaterialList().addMaterial(new Lumber(length, studType), 1);

        this.platesHeightMap = new TreeMap<>();
        Plate genericTopPlate = new Plate(length, studType);
        this.platesHeightMap.put(new Measurement(0), genericTopPlate);

        // Add the bottom plate
        this.platesHeightMap.put(height.subtract(studType.width), new Plate(length, studType));
        this.plateMaterials.addMaterials(genericTopPlate.materialList());

        // if loadBearing, there are two top plates
        if (loadBearing) {
            this.platesHeightMap.put(studType.width, genericTopPlate);
            this.plateMaterials.addMaterials(genericTopPlate.materialList());
            this.studHeightShift = studType.width.multiply(2);
        }
        // otherwise, just one
        else {
            this.studHeightShift = studType.width;
        }

        // The height of a wall includes the top and bottom plates and the studs, so we need to remove the height of the
        // plates to get the heights of the studs
        Measurement heightOfAllPlates = studType.width.multiply(this.platesHeightMap.size());
        this.studHeight = validateParameterMinimumMeasurement(height, heightOfAllPlates, "height").subtract(heightOfAllPlates);
        this.stud = new Stud(this.studHeight, studType);
        this.layout = this.createLayout(validateParameterMinimumMeasurement(length, studType.width.multiply(minimumNumberOfStuds),
                "length"));
    }

    /**
     * A convenience constructor when creating a standard 8', load bearing wall.
     * @param length - Length of wall to create
     * @throws IllegalArgumentException - Thrown when the length is less than the minimum wall length/height
     */
    Wall(Measurement length) throws IllegalArgumentException {
        this(length, new Measurement(97, Measurement.Fraction.ONE_EIGHTH));
    }

    private static Measurement validateParameterMinimumMeasurement(Measurement parameter, Measurement minimumValue, String type)
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
        currentPosition = currentPosition.add(studSeparation);

        // add the rest of the studs. There needs to be enough room for both the full separation and the width of
        // two studs, the stud to add and the final stud
        Measurement furthestAdditionalStud = length.subtract(studType.width.multiply(2));
        while (currentPosition.compareTo(furthestAdditionalStud) < 0) {
            currentLayout.addStudAt(currentPosition, this.stud);
            currentPosition = currentPosition.add(studSeparation);
        }

        // It possible for the difference between the last added stud and the final stud to be greater than the
        // studSeparation. In this case, we need a stud to be right next to the final stud in the location of
        // furthestAdditionalStud.
        Measurement finalStudPosition = length.subtract(studType.width);

        // Since Measurements have to be positive, the currentPosition needs to be backed up by a studSeparation
        // to ensure finalStudPosition.subtract(currentPosition) won't ever be negative. Then the difference can
        // be compared to the studSeparation to see which is bigger
        if (finalStudPosition.subtract(currentPosition.subtract(studSeparation)).compareTo(studSeparation) > 0) {

            // A double stud here will ensure the correct number of nails are added
            currentLayout.addStudAt(furthestAdditionalStud, new DoubleStud(this.stud));
        }
        else {
            // add the final stud, so it touches the end of the wall
            currentLayout.addStudAt(finalStudPosition, this.stud);
        }
        return currentLayout;
    }

    /**
     *
     * @return The studs required to create this wall
     */
    Layout layout() {
        return this.layout;
    }

    public Wall addADoor(Door.StandardDoor ofType, Measurement atLocation) throws IllegalArgumentException {
        String baseErrorMessage = String.format("Door of type %s cannot be installed at %s.", ofType, atLocation);
        Door attemptedDoor = new Door(ofType);
        if (this.studHeight.compareTo(attemptedDoor.totalHeight()) < 0 ) {
            throw new IllegalArgumentException(baseErrorMessage
                    + String.format(" Wall has studs %s tall, door was %s tall", this.studHeight, attemptedDoor.totalHeight()));
        }
        try {
            this.layout.addDoorAt(attemptedDoor, atLocation);
        }
        catch (Layout.InstallableLocationConflict c) {
            throw new IllegalArgumentException(baseErrorMessage
                    + String.format(" Wall is only %s long, door at %s of width %s would be outside the wall",
                        this.length,
                        atLocation,
                        attemptedDoor.totalWidth()));
        }
        return this;
    }

    /**
     *
     * @return The material required to build this wall
     */
    @Override
    public Vector<Vector<String>> materials() {
        return this.materialList().materials();
    }

    /**
     *
     * @return The instructions to draw this wall as described in {@link Buildable}
     * @see Buildable
     */
    @Override
    public Vector<Vector<Vector<Integer>>> drawingInstructions() { return this.graphicsList().drawingInstructions(); }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Wall
     */
    @Override
    public Measurement totalWidth() {
        return this.layout.totalWidth();
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the height of this Wall
     */
    @Override
    public Measurement totalHeight() {
        return this.layout.totalHeight();
    }

    /**
     *
     * @return - A {@link MaterialList} of {@link Models.Buildable.Material.Material} used to create this Wall
     */
    @Override
    public MaterialList materialList() {
        return new MaterialList().addMaterials(this.plateMaterials).addMaterials(this.layout.materialList());
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Wall
     */
    @Override
    public GraphicsList graphicsList() {
        GraphicsList result = new GraphicsList();
        Measurement zero = new Measurement(0);

        // Plates need to be shifted according to where it is located in the wall
        this.platesHeightMap.forEach((measurement,plate) -> result.addGraphics(plate.graphicsList().shift(zero, measurement)));

        result.addGraphics(this.layout.graphicsList().shift(zero, this.studHeightShift));
        return result;
    }
}

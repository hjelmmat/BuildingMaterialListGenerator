package Models.Buildable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Installable.Door;
import Models.Buildable.Installable.DoubleStud;
import Models.Buildable.Installable.Layout;
import Models.Buildable.Installable.Stud;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {
    Measurement validMeasurement = new Measurement(100);
    Wall maxTwoStudWall = new Wall(new Measurement(17, Measurement.Fraction.ONE_HALF));
    Wall threeStudWall = new Wall(new Measurement(25));
    Wall fourStudWall = new Wall(new Measurement(40));

    @Test
    public void wallShouldThrowWhenLengthIsInvalid() {
        Measurement minimumLengthEdgeCase = new Measurement(2, Measurement.Fraction.FIFTEEN_SIXTEENTH);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                                        () -> new Wall(minimumLengthEdgeCase));
        String exceptionMessage = "length cannot be less than 3\", was 2-15/16\"";
        assertEquals(exceptionMessage, thrown.getMessage());
        assertDoesNotThrow(() -> new Wall(new Measurement(3)));
    }

    @Test
    public void wallShouldThrowWhenHeightIsInvalid() {
        Measurement minimumHeightEdgeCase = new Measurement(2, Measurement.Fraction.FIFTEEN_SIXTEENTH);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                                        () -> new Wall(this.validMeasurement, minimumHeightEdgeCase));
        String exceptionMessage = "height cannot be less than 4-1/2\", was 2-15/16\"";
        assertEquals(exceptionMessage, thrown.getMessage());
        Measurement minimumHeight = new Measurement(5);
        assertDoesNotThrow(() -> new Wall(this.validMeasurement, minimumHeight));
    }

    @Test
    public void wallShouldCreateWallWithDefaultHeight() {
        Vector<Vector<String>> results = new Vector<>();
        results.add(new Vector<>(List.of("Material", "Quantity")));
        results.add(new Vector<>(List.of("24\" 2x4", "3")));
        results.add(new Vector<>(List.of("92-5/8\" 2x4", "3")));
        results.add(new Vector<>(List.of("10d nails", "30")));
        assertEquals(results, new Wall(new Measurement(24)).materials());
    }

    @Test
    public void wallShouldCalculateCorrectStuds() throws IllegalArgumentException {
        Stud standardStud = new Stud();
        Layout minimumLayout = new Layout().addStudAt(new Measurement(0), standardStud)
                .addStudAt(new Measurement(1, Measurement.Fraction.ONE_HALF), standardStud);
        assertEquals(minimumLayout, new Wall(new Measurement(3)).layout());

        Measurement firstStud = new Measurement(0);
        Measurement secondStud = new Measurement(16);
        Layout twoStudLayout = new Layout().addStudAt(firstStud, standardStud).addStudAt(secondStud, standardStud);
        assertEquals(twoStudLayout, this.maxTwoStudWall.layout());

        DoubleStud doubleStud = new DoubleStud(standardStud);
        Layout minThreeStudLayout = new Layout()
                .addStudAt(firstStud, standardStud)
                .addStudAt(new Measurement(14, Measurement.Fraction.NINE_SIXTEENTH), doubleStud);
        Wall minThreeStudWall = new Wall(new Measurement(17, Measurement.Fraction.NINE_SIXTEENTH));
        assertEquals(minThreeStudLayout, minThreeStudWall.layout());

        Layout threeStudLayout = new Layout().addStudAt(firstStud, standardStud)
                .addStudAt(secondStud, standardStud)
                .addStudAt(new Measurement(23, Measurement.Fraction.ONE_HALF), standardStud);
        assertEquals(threeStudLayout, this.threeStudWall.layout());

        Layout fourStudLayout = new Layout()
                .addStudAt(firstStud, standardStud)
                .addStudAt(secondStud, standardStud)
                .addStudAt(new Measurement(32), standardStud)
                .addStudAt(new Measurement(38, Measurement.Fraction.ONE_HALF), standardStud);
        assertEquals(fourStudLayout, this.fourStudWall.layout());
    }

    @Test
    public void wallShouldCalculateCorrectStudHeightForLayout() {
        Stud shortStud = new Stud(new Measurement(5), Lumber.Dimension.TWO_BY_FOUR);
        Layout shortLayout = new Layout().addStudAt(new Measurement(0), shortStud)
                .addStudAt(new Measurement(1, Measurement.Fraction.ONE_HALF), shortStud);
        assertEquals(shortLayout, new Wall(new Measurement(3), new Measurement(9, Measurement.Fraction.ONE_HALF)).layout());
    }

    @Test
    public void wallShouldCalculateDrawingInstructions() {
        Measurement zero = new Measurement(0);
        Measurement width = Lumber.Dimension.TWO_BY_FOUR.width;
        Measurement ten = new Measurement(10);
        Vector<Vector<Vector<Integer>>> result = new Vector<>();
        result.add(new Vector<>());

        Vector<Vector<Integer>> rectangles = new Vector<>();
        // Add the plates
        rectangles.add(new Vector<>(List.of(zero.numberOfPixels(), zero.numberOfPixels(), ten.numberOfPixels(), width.numberOfPixels())));
        rectangles.add(new Vector<>(List.of(zero.numberOfPixels(), width.numberOfPixels(), ten.numberOfPixels(), width.numberOfPixels())));
        Measurement heightMinusWidth = ten.subtract(width);
        rectangles.add(new Vector<>(List.of(zero.numberOfPixels(), heightMinusWidth.numberOfPixels(), ten.numberOfPixels(), width.numberOfPixels())));

        // Add the studs
        Measurement studHeight = ten.subtract(width.multiply(3));
        Measurement doubleWidth = width.multiply(2);
        rectangles.add(new Vector<>(List.of(zero.numberOfPixels(), doubleWidth.numberOfPixels(), width.numberOfPixels(), studHeight.numberOfPixels())));
        rectangles.add(new Vector<>(List.of(ten.subtract(width).numberOfPixels(), doubleWidth.numberOfPixels(), width.numberOfPixels(), studHeight.numberOfPixels())));
        result.add(rectangles);

        assertEquals(result, new Wall(ten, ten).drawingInstructions());
    }

    @Test
    public void shouldThrowExceptionWhenDoorDoesNotFitInWallLength() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> this.maxTwoStudWall.addADoor(Door.StandardDoor.Bedroom, new Measurement(100)));
        String exceptionMessage = "Door of type Bedroom cannot be installed at 100\". Wall is only 17-1/2\" long, door at 100\" of width 44\" would be outside the wall";
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDoorIsTooTallForWall() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Wall(new Measurement(100), new Measurement(10)).addADoor(Door.StandardDoor.Bedroom, new Measurement(0)));
        assertEquals("Door of type Bedroom cannot be installed at 0\". Wall has studs 5-1/2\" tall, door was 89-1/2\" tall",
                thrown.getMessage());
    }

    @Test
     public void whenDoorIsAddedMaterialListShouldUpdate() {
        Measurement defaultHeight = new Measurement(92, Measurement.Fraction.FIVE_EIGHTH);
        MaterialList materialResult = new MaterialList()
                .addMaterial(Nail.TEN_D, 168)
                .addMaterial(new Lumber(defaultHeight, Lumber.Dimension.TWO_BY_FOUR), 4)
                .addMaterial(new Lumber(new Measurement(81), Lumber.Dimension.TWO_BY_FOUR), 2)
                .addMaterial(new Lumber(new Measurement(41), Lumber.Dimension.TWO_BY_FOUR), 5)
                .addMaterial(new Lumber(new Measurement(41), Lumber.Dimension.TWO_BY_SIX), 2)
                .addMaterial(new Lumber(new Measurement(6, Measurement.Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR), 4);

        Measurement wallLength = new Measurement(48);
        Wall test = new Wall(wallLength).addADoor(Door.StandardDoor.Bedroom, new Measurement(2));

        /*
        Checking twice to ensure that the materials are not added multiple times over successive calls`
         */
        assertEquals(materialResult, test.materialList());
        assertEquals(materialResult, test.materialList());

        Measurement zero = new Measurement(0);
        Measurement studWidth = new Stud().totalWidth();
        Measurement downShiftBecauseOfPlates = studWidth.multiply(2);
        GraphicsList graphicsResult = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, wallLength, studWidth))
                .addGraphic(new RectangleInstructions(zero, studWidth, wallLength, studWidth))
                .addGraphic(new RectangleInstructions(zero, downShiftBecauseOfPlates.add(defaultHeight), wallLength, studWidth))
                .addGraphic(new RectangleInstructions(zero, downShiftBecauseOfPlates, studWidth, defaultHeight))
                .addGraphic(new RectangleInstructions(wallLength.subtract(studWidth), downShiftBecauseOfPlates, studWidth, defaultHeight))
                .addGraphics(new Door()
                    .addCrippleStud(new Stud(), new Measurement(14))
                    .addCrippleStud(new Stud(), new Measurement(30))
                    .graphicsList().shift(new Measurement(2), downShiftBecauseOfPlates));
        assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions());
    }
}

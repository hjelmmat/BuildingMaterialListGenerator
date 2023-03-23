package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class LayoutTest {
    Stud defaultStud = new Stud();
    Measurement firstPosition = new Measurement(0);
    Measurement secondPosition = new Measurement(1, Measurement.Fraction.ONE_HALF);
    Layout basicLayout = new Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud);

    @Test
    public void layoutShouldAddStudWithPositionCopy() {
        Measurement position = new Measurement(0);
        assertEquals(basicLayout, new Layout().addStudAt(position, this.defaultStud).
                addStudAt(position.add(this.secondPosition), this.defaultStud));
    }

    @Test
    public void layoutShouldThrowWhenConflictingStudIsAdded() {
        String error = "Stud cannot be added at 2\". Stud already is located at 1\"";
        Layout.InstallableLocationConflict thrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Layout().addStudAt(new Measurement(1), defaultStud)
                        .addStudAt(new Measurement(2), defaultStud));
        assertEquals(error, thrown.getMessage());
        assertEquals(new Measurement(1), thrown.conflict);

        String secondError = "Stud cannot be added at 2\". Stud already is located at 2\"";
        Layout.InstallableLocationConflict secondThrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Layout().addStudAt(new Measurement(2), defaultStud)
                        .addStudAt(new Measurement(2), defaultStud));
        assertEquals(secondError, secondThrown.getMessage());
        assertEquals(new Measurement(2), secondThrown.conflict);

        String thirdError = "Stud cannot be added at 4-1/2\". Stud already is located at 5\"";
        Layout.InstallableLocationConflict thirdThrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Layout().addStudAt(new Measurement(5), defaultStud)
                        .addStudAt(new Measurement(4, Measurement.Fraction.ONE_HALF), defaultStud));
        assertEquals(thirdError, thirdThrown.getMessage());
        assertEquals(new Measurement(5), thirdThrown.conflict);
    }

    @Test
    public void layoutShouldReturnMaterials() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 12)
                .addMaterial(new Lumber(new Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2);
        assertEquals(result, this.basicLayout.materialList());
    }

    @Test
    public void layoutShouldDetermineEquals() {
        Layout resultLayout = new Layout()
                .addStudAt(firstPosition, defaultStud)
                .addStudAt(secondPosition, defaultStud)
                .addStudAt(new Measurement(100), defaultStud)
                .addDoorAt(new Door(), new Measurement(10));
        Layout testingLayout = new Layout()
                .addStudAt(firstPosition, defaultStud)
                .addStudAt(secondPosition, defaultStud)
                .addStudAt(new Measurement(100), defaultStud)
                .addDoorAt(new Door(), new Measurement(10));
        assertEquals(resultLayout, testingLayout);
    }

    @Test
    public void layoutShouldCreateGraphicsList() {
        Measurement width = Lumber.Dimension.TWO_BY_FOUR.width;
        Measurement zero = new Measurement(0);
        Measurement distance = new Measurement(10);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, width, distance))
                .addGraphic(new RectangleInstructions(distance, zero, width, distance));
        Stud stud = new Stud(distance, Lumber.Dimension.TWO_BY_FOUR);
        assertEquals(result.drawingInstructions(),
                new Layout().addStudAt(new Measurement(0), stud).addStudAt(distance, stud).graphicsList().drawingInstructions());
    }

    @Test
    public void whenADoorIsAddedLayoutShouldReplaceStudsInRangeWithDoor() {
        Measurement zero = new Measurement(0);
        Door resultDoor = new Door().addCrippleStud(defaultStud, zero);
        Layout result = new Layout().addStudAt(new Measurement(100), defaultStud).addDoorAt(resultDoor, zero);
        assertEquals(result, new Layout()
                .addStudAt(new Measurement(100), defaultStud)
                .addStudAt(zero, defaultStud)
                .addDoorAt(new Door(), zero));
    }

    @Test
    public void whenADoorIsAddedThatOverlapsAnotherDoorAddDoorAtShouldThrow() {
        String error = "Door cannot be added at 44\". Door already is located at 1\"";
        Layout.InstallableLocationConflict thrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Layout().addStudAt(new Measurement(100), defaultStud).addDoorAt(new Door(), new Measurement(1))
                        .addDoorAt(new Door(), new Measurement(44)));
        assertEquals(error, thrown.getMessage());
        assertEquals(new Measurement(1), thrown.conflict);

        String secondError = "Door cannot be added at 44\". Door already is located at 45\"";
        Layout.InstallableLocationConflict secondThrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Layout().addStudAt(new Measurement(100), defaultStud).addDoorAt(new Door(), new Measurement(45))
                        .addDoorAt(new Door(), new Measurement(44)));
        assertEquals(secondError, secondThrown.getMessage());
        assertEquals(new Measurement(45), secondThrown.conflict);
    }

    @Test
    public void whenStudsAreDifferentHeightsGraphicListShouldDrawCorrectly() {
        Measurement width = Lumber.Dimension.TWO_BY_FOUR.width;
        Measurement zero = new Measurement(0);
        Measurement distance = new Measurement(10);
        Measurement shorterDistance = new Measurement(8);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, width, distance))
                .addGraphic(new RectangleInstructions(distance, new Measurement(2), width, shorterDistance));
        Stud stud = new Stud(distance, Lumber.Dimension.TWO_BY_FOUR);
        assertEquals(result.drawingInstructions(), new Layout()
                .addStudAt(new Measurement(0), stud)
                .addStudAt(distance, new Stud(shorterDistance, Lumber.Dimension.TWO_BY_FOUR))
                .graphicsList()
                .drawingInstructions());
    }

    @Test
    public void layoutShouldReturnTotalWidth() {
        Measurement result = new Measurement(10);
        Layout test = new Layout().addStudAt(new Measurement(8, Measurement.Fraction.ONE_HALF), defaultStud);
        assertEquals(result, test.totalWidth());

        Measurement secondResult = new Measurement(50);
        test.addStudAt(new Measurement(48, Measurement.Fraction.ONE_HALF), defaultStud).addDoorAt(new Door(), new Measurement(4, Measurement.Fraction.ONE_HALF));
        assertEquals(secondResult, test.totalWidth());

        Measurement thirdResult = new Measurement(55);
        test.addStudAt(new Measurement(53, Measurement.Fraction.ONE_HALF), defaultStud);
        assertEquals(thirdResult, test.totalWidth());
    }

    @Test
    public void shouldReturnIsEmpty() {
        assertTrue(new Layout().isEmpty());
        assertFalse(new Layout().addStudAt(new Measurement(0), defaultStud).isEmpty());
    }

    @Test
    public void layoutShouldAddDoor() {
        Measurement zero = new Measurement(0);
        Layout test = new Layout().addStudAt(zero, defaultStud)
                .addStudAt(new Measurement(16), defaultStud)
                .addStudAt(new Measurement(32), defaultStud)
                .addStudAt(new Measurement(50), defaultStud)
                .addDoorAt(new Door(), new Measurement(2));

        MaterialList materialResult = new MaterialList()
                .addMaterial(Nail.TEN_D, 148)
                .addMaterial(new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), Lumber.Dimension.TWO_BY_FOUR), 4)
                .addMaterial(new Lumber(new Measurement(81), Lumber.Dimension.TWO_BY_FOUR), 2)
                .addMaterial(new Lumber(new Measurement(41), Lumber.Dimension.TWO_BY_FOUR), 2)
                .addMaterial(new Lumber(new Measurement(41), Lumber.Dimension.TWO_BY_SIX), 2)
                .addMaterial(new Lumber(new Measurement(6, Measurement.Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR), 4);
        assertEquals(materialResult.materials(), test.materialList().materials());

        GraphicsList graphicsResult = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, defaultStud.totalWidth(), defaultStud.installedHeight))
                .addGraphic(new RectangleInstructions(new Measurement(50), zero, defaultStud.totalWidth(), defaultStud.installedHeight));
        graphicsResult.addGraphics(new Door()
                .addCrippleStud(defaultStud, new Measurement(14))
                .addCrippleStud(defaultStud, new Measurement(30))
                .graphicsList().shift(new Measurement(2), zero));
        assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions());
    }

    @Test
    public void layoutShouldAdjustOutsideStudsForAddedDoors() {
        Layout result = new Layout().addStudAt(new Measurement(4, Measurement.Fraction.ONE_HALF), defaultStud)
                .addStudAt(new Measurement(50), defaultStud)
                .addStudAt(new Measurement(100), defaultStud)
                .addDoorAt(new Door().addCrippleStud(defaultStud, new Measurement(24)), new Measurement(6));
        Layout test = new Layout().addStudAt(new Measurement(5), defaultStud)
                .addStudAt(new Measurement(30), defaultStud)
                .addStudAt(new Measurement(49), defaultStud)
                .addStudAt(new Measurement(100), defaultStud)
                .addDoorAt(new Door(), new Measurement(6));
        assertEquals(result.graphicsList().drawingInstructions(), test.graphicsList().drawingInstructions());
    }

    @Test
    public void addDoorShouldThrowIfStudCannotBeMoved() {
        String error = "Door cannot be added at 1\". Stud at 0\" cannot be moved.";
        Layout test = new Layout().addStudAt(new Measurement(0),defaultStud).addStudAt(new Measurement(100), defaultStud);
        Layout.InstallableLocationConflict thrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> test.addDoorAt(new Door(), new Measurement(1)));
        assertEquals(error, thrown.getMessage());
        assertEquals(new Measurement(0), thrown.conflict);
        assertEquals(new Layout().addStudAt(new Measurement(0), defaultStud).addStudAt(new Measurement(100), defaultStud), test);

        String secondError = "Door cannot be added at 0\". Stud at 43\" cannot be moved.";
        Layout.InstallableLocationConflict secondThrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Layout()
                        .addStudAt(new Measurement(43) ,defaultStud)
                        .addDoorAt(new Door(), new Measurement(0)));
        assertEquals(secondError, secondThrown.getMessage());
        assertEquals(new Measurement(43), secondThrown.conflict);
    }

    @Test
    public void addDoorShouldThrowWhenRightSideOfDoorIsOutsideLayout() {
        String error = "Door cannot be added at 0\". Layout is only 11-1/2\" long and door ends at 44\"";
        Layout test = new Layout().addStudAt(new Measurement(10), defaultStud);
        Layout.InstallableLocationConflict thrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> test.addDoorAt(new Door(), new Measurement(0)));
        assertEquals(error, thrown.getMessage());
        assertEquals(new Measurement(0), thrown.conflict);

        test.addStudAt(new Measurement(44), defaultStud);
        assertDoesNotThrow(() -> test.addDoorAt(new Door(), new Measurement(0)));
    }
}
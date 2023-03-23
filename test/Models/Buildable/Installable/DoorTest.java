package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoorTest {
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    Measurement baseKingHeight = new Measurement(89, Measurement.Fraction.ONE_HALF);
    Measurement baseTrimmerHeight = new Measurement(81);
    Measurement gapWidth = new Measurement(41);
    Measurement baseTotalWidth = new Measurement(44);


    @Test
    public void shouldEqualsWhenCrippleStudsAndTypeAreTheSame() {
        Door test = new Door(Door.StandardDoor.Bedroom);
        assertEquals(new Door(Door.StandardDoor.Bedroom), test);

        assertEquals(new Door(Door.StandardDoor.Bedroom).addCrippleStud(new Stud(), new Measurement(4)),
                test.addCrippleStud(new Stud(), new Measurement(4)));

        assertNotEquals(new Door(Door.StandardDoor.Bedroom)
                .addCrippleStud(new Stud(new Measurement(100), dimension), new Measurement(4)),
                test);
    }

    @Test
    public void shouldCreateHashNumber() {
        int result = 1452012306;
        assertEquals(result, new Door().hashCode());
    }

    @Test
    public void shouldCreateMaterialList() {
        MaterialList result = new MaterialList()
                .addMaterial(new Lumber(baseKingHeight, dimension), 2)
                .addMaterial(new Lumber(baseTrimmerHeight, dimension), 2)
                .addMaterial(new Lumber(gapWidth, dimension), 2)
                .addMaterial(new Lumber(gapWidth, Lumber.Dimension.TWO_BY_SIX), 2)
                .addMaterial(Nail.TEN_D, 104);
        assertEquals(result, new Door().materialList());
    }

    @Test
    public void shouldAddCrippleStuds() {
        Measurement zero = new Measurement(0);
        Measurement topOfDoor = new Measurement(100);
        Stud kingStud = new Stud(topOfDoor, dimension);
        Measurement topOfTrimmer = kingStud.installedHeight.clone().subtract(baseTrimmerHeight);
        Measurement lastTrimmerPlacement = baseTotalWidth.clone().subtract(dimension.width.clone().multiply(2));
        Header header = new Header(gapWidth);
        Measurement topOfHeader = topOfTrimmer.clone().subtract(header.height);
        Measurement crippleDistance = new Measurement(10);
        GraphicsList result = new GraphicsList()
                .addGraphics(kingStud.graphicsList())
                .addGraphic(new RectangleInstructions(dimension.width, topOfTrimmer, dimension.width, baseTrimmerHeight))
                .addGraphic(new RectangleInstructions(lastTrimmerPlacement, topOfTrimmer, dimension.width, baseTrimmerHeight))
                .addGraphics(kingStud.graphicsList().shift(baseTotalWidth.clone().subtract(dimension.width), zero))
                .addGraphics(header.graphicsList().shift(dimension.width, topOfHeader))
                .addGraphic(new RectangleInstructions(dimension.width, zero, dimension.width, topOfHeader))
                .addGraphic(new RectangleInstructions(dimension.width.clone().multiply(2), zero, dimension.width, topOfHeader))
                .addGraphic(new RectangleInstructions(crippleDistance, zero, dimension.width, topOfHeader))
                .addGraphic(new RectangleInstructions(lastTrimmerPlacement.clone().subtract(dimension.width), zero, dimension.width, topOfHeader))
                .addGraphic(new RectangleInstructions(lastTrimmerPlacement, zero, dimension.width, topOfHeader));

        Stud crippleStuds = new Stud(topOfDoor, Lumber.Dimension.TWO_BY_FOUR);
        Door test = new Door()
                // Valid Cripple Stud
                .addCrippleStud(crippleStuds, crippleDistance)
                // Ignored Cripple Stud since it is where the left king stud is
                .addCrippleStud(crippleStuds, zero)
                // Ignored Cripple Stud since it is exactly where the first cripple stud is
                .addCrippleStud(crippleStuds, dimension.width)
                // Ignored Cripple Stud since it is where the right king stud is
                .addCrippleStud(crippleStuds, baseTotalWidth.clone().subtract(dimension.width).add(new Measurement(1)))
                // Ignored Cripple Stud since it is exactly where the last cripple stud is
                .addCrippleStud(crippleStuds, baseTotalWidth.clone().subtract(dimension.width.clone().multiply(2)))
                // Valid Cripple Stud that needs to be shifted Right
                .addCrippleStud(crippleStuds, dimension.width.clone().add(new Measurement(1)))
                // Valid Cripple Stud that needs to be shifted Left
                .addCrippleStud(crippleStuds, lastTrimmerPlacement.clone().subtract(new Measurement(1)));
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions());

        MaterialList materialResult = new MaterialList()
                .addMaterial(new Lumber(topOfDoor, dimension), 2)
                .addMaterial(new Lumber(baseTrimmerHeight, dimension), 2)
                .addMaterial(new Lumber(gapWidth, dimension), 2)
                .addMaterial(new Lumber(gapWidth, Lumber.Dimension.TWO_BY_SIX), 2)
                .addMaterial(new Lumber(crippleDistance, dimension), 5)
                .addMaterial(Nail.TEN_D, 150);
        assertEquals(materialResult, test.materialList());
    }

    @Test
    public void shouldCreateGraphicsList() {
        Measurement zero = new Measurement(0);
        Measurement topOfTrimmer = baseKingHeight.clone().subtract(baseTrimmerHeight);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, dimension.width, baseKingHeight))
                .addGraphic(new RectangleInstructions(dimension.width, topOfTrimmer, dimension.width, baseTrimmerHeight))
                .addGraphic(new RectangleInstructions(baseTotalWidth.clone().subtract(dimension.width.clone().multiply(2)), topOfTrimmer, dimension.width, baseTrimmerHeight))
                .addGraphic(new RectangleInstructions(baseTotalWidth.clone().subtract(dimension.width), zero, dimension.width, baseKingHeight))
                .addGraphics(new Header(gapWidth).graphicsList().shift(dimension.width, zero));
        assertEquals(result.drawingInstructions(), new Door().graphicsList().drawingInstructions());
    }

    @Test
    public void whenCrippleStudIsAddedThatConflictsWithPreviousCrippleLayoutShouldThrow() {
        String error = "Stud cannot be added at 10\". Stud already is located at 10\"";
        Layout.InstallableLocationConflict thrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Door()
                .addCrippleStud(new Stud(), new Measurement(10))
                .addCrippleStud(new Stud(), new Measurement(10)));
        assertEquals(error, thrown.getMessage());
        assertEquals(new Measurement(10), thrown.conflict);
    }

    @Test
    public void shouldThrowWhenCrippleStudIsAddedBeyondEdgeOfDoor() {
        String error = "Stud cannot be added at 44-1/16\". Door is only 44\" long";
        Measurement conflictMeasurement = new Measurement(44, Measurement.Fraction.ONE_SIXTEENTH);
        Layout.InstallableLocationConflict thrown = assertThrows(Layout.InstallableLocationConflict.class,
                () -> new Door().addCrippleStud(new Stud(), conflictMeasurement));
        assertEquals(error, thrown.getMessage());
        assertEquals(conflictMeasurement, thrown.conflict);
    }
}
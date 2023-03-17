package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlateTest {
    @Test
    public void plateShouldCalculateMaterial() {
        Measurement firstLength = new Measurement(3);
        Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 4)
                .addMaterial(new Lumber(firstLength, dimension), 1);
        assertEquals(result, new Plate(firstLength, dimension).materialList());
        assertEquals(result, new Plate(new Measurement(12), dimension).materialList());

        Measurement secondLength = new Measurement(12, Measurement.Fraction.ONE_SIXTEENTH);
        MaterialList secondResult = new MaterialList().addMaterial(Nail.TEN_D, 6)
                .addMaterial(new Lumber(secondLength, dimension), 1);
        assertEquals(secondResult, new Plate(secondLength, dimension).materialList());
        assertEquals(secondResult, new Plate(new Measurement(24), dimension).materialList());

        Measurement thirdLength = new Measurement(24, Measurement.Fraction.ONE_SIXTEENTH);
        MaterialList thirdResult = new MaterialList().addMaterial(Nail.TEN_D, 8)
                .addMaterial(new Lumber(thirdLength, dimension), 1);
        assertEquals(thirdResult, new Plate(thirdLength, dimension).materialList());
        assertEquals(thirdResult, new Plate(new Measurement(36), dimension).materialList());
    }

    @Test
    public void plateShouldCalculateEqual() {
        Plate threePlate = new Plate(new Measurement(3), Lumber.Dimension.TWO_BY_FOUR);
        assertEquals(threePlate,
                new Plate(new Measurement(3), Lumber.Dimension.TWO_BY_FOUR));
        assertNotEquals(threePlate, new Plate(new Measurement(5), Lumber.Dimension.TWO_BY_FOUR));
    }

    @Test
    public void plateShouldCalculateHashCode() {
        int result = 1768948330;
        assertEquals(result, new Plate(new Measurement(30), Lumber.Dimension.TWO_BY_FOUR).hashCode());
    }

    @Test
    public void plateShouldCreateGraphicsList() {
        Measurement zero = new Measurement(0);
        Measurement ten = new Measurement(10);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, ten, Lumber.Dimension.TWO_BY_FOUR.width));
        Plate test = new Plate(ten, Lumber.Dimension.TWO_BY_FOUR);
        assertEquals(result.drawingInstructions(), test.drawingInstructions().drawingInstructions());
    }

    @Test
    public void plateShouldCreateLineBelowForTopPlate() {
        Measurement zero = new Measurement(0);
        Measurement ten = new Measurement(10);
        Measurement width = Lumber.Dimension.TWO_BY_FOUR.width;
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, ten, Lumber.Dimension.TWO_BY_FOUR.width));
        Plate test = new Plate(ten, Lumber.Dimension.TWO_BY_FOUR);
        assertEquals(result.drawingInstructions(), test.drawingInstructions().drawingInstructions());
    }
}
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
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    @Test
    public void plateShouldCalculateMaterial() {
        Measurement firstLength = new Measurement(3);
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
    public void plateShouldCreateGraphicsList() {
        Measurement zero = new Measurement(0);
        Measurement ten = new Measurement(10);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, ten, this.dimension.width));
        Plate test = new Plate(ten, this.dimension);
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions());
    }

    @Test
    public void plateShouldReturnTotalWidth() {
        Measurement result = new Measurement(10);
        assertEquals(result, new Plate(result, this.dimension).totalWidth());
    }

    @Test
    public void plateShouldCalculateNumberOfNails() {
        assertEquals(4, Plate.numberOfNails(new Measurement(12)));
        assertEquals(6, Plate.numberOfNails(new Measurement(12, Measurement.Fraction.ONE_SIXTEENTH)));
        assertEquals(6, Plate.numberOfNails(new Measurement(24)));
        assertEquals(8, Plate.numberOfNails(new Measurement(24, Measurement.Fraction.ONE_SIXTEENTH)));
    }
}
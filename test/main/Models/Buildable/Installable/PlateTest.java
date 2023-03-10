package main.Models.Buildable.Installable;

import main.Models.Buildable.Material.Lumber;
import main.Models.Buildable.Material.MaterialList;
import main.Models.Buildable.Material.Nail;
import main.Models.Measurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlateTest {
    @Test
    public void plateShouldCalculateMaterial() {
        Measurement firstLength = new Measurement(3);
        Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 4)
                .addMaterial(new Lumber(firstLength, dimension), 1);
        assertEquals(result, new Plate(firstLength, dimension).material());
        assertEquals(result, new Plate(new Measurement(12), dimension).material());

        Measurement secondLength = new Measurement(12, Measurement.Fraction.ONE_SIXTEENTH);
        MaterialList secondResult = new MaterialList().addMaterial(Nail.TEN_D, 6)
                .addMaterial(new Lumber(secondLength, dimension), 1);
        assertEquals(secondResult, new Plate(secondLength, dimension).material());
        assertEquals(secondResult, new Plate(new Measurement(24), dimension).material());

        Measurement thirdLength = new Measurement(24, Measurement.Fraction.ONE_SIXTEENTH);
        MaterialList thirdResult = new MaterialList().addMaterial(Nail.TEN_D, 8)
                .addMaterial(new Lumber(thirdLength, dimension), 1);
        assertEquals(thirdResult, new Plate(thirdLength, dimension).material());
        assertEquals(thirdResult, new Plate(new Measurement(36), dimension).material());
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
        int result = -1986091983;
        assertEquals(result, new Plate(new Measurement(30), Lumber.Dimension.TWO_BY_FOUR).hashCode());
    }
}
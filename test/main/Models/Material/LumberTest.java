package main.Models.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LumberTest {
    @Test
    public void lumberShouldThrowWhenLengthIsTooLong() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Lumber(new Measurement(240, Measurement.Fraction.ONE_SIXTEENTH), Lumber.Dimension.TWO_BY_FOUR));
        String message = "A FactoryLength cannot be greater than 240\", was actually 240-1/16\"";
        assertEquals(message, thrown.getMessage());

        assertDoesNotThrow(() -> new Lumber(new Measurement(240), Lumber.Dimension.TWO_BY_FOUR));
    }

    @Test
    public void lumberShouldCompareEqual() {
        Lumber lumber1 = new Lumber(new Measurement(3), Lumber.Dimension.TWO_BY_FOUR);
        Lumber lumber2 = new Lumber(new Measurement(3), Lumber.Dimension.TWO_BY_FOUR);
        assertTrue(() -> lumber1.equals(lumber2));

        Lumber lumber3 = new Lumber(new Measurement(10), Lumber.Dimension.TWO_BY_FOUR);
        assertTrue(() -> lumber1.equals(lumber3));

        Lumber lumber4 = new Lumber(new Measurement(50), Lumber.Dimension.TWO_BY_FOUR);
        assertFalse(() -> lumber1.equals(lumber4));
    }

    @Test
    public void lumberShouldProduceUniqueHash() {
        int result = -196223298;
        assertEquals(result, new Lumber(new Measurement(3), Lumber.Dimension.TWO_BY_FOUR).hashCode());

        int secondResult = -572052698;
        assertEquals(secondResult, new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH),
                Lumber.Dimension.TWO_BY_FOUR).hashCode());
    }
    
    @Test
    public void lumberShouldProduceMaterialList() {
        MaterialList result = new MaterialList().addMaterial(new Lumber(new Measurement(24), Lumber.Dimension.TWO_BY_FOUR), 1);
        assertEquals(result, new Lumber(new Measurement(5), Lumber.Dimension.TWO_BY_FOUR).material());

        MaterialList secondResult = new MaterialList().addMaterial(new Lumber(new Measurement(192), Lumber.Dimension.TWO_BY_FOUR), 1);
        assertEquals(secondResult, new Lumber(new Measurement(190), Lumber.Dimension.TWO_BY_FOUR).material());
    }
}
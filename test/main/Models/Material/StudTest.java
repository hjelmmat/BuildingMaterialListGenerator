package main.Models.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudTest {
    @Test
    public void defaultStudShouldHave8FtLength() {
        Measurement result = new Measurement(92, Measurement.Fraction.FIVE_EIGHTH);
        assertEquals(result, new Stud().installedLength);
    }

    @Test
    public void studShouldProduceMaterialList() {
        MaterialList result = new MaterialList().addMaterial(Nails.TEN_D, 6)
                .addMaterial(new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), Stud.Dimension.TWO_BY_FOUR), 1);
        assertEquals(result, new Stud().material());
        assertEquals(result, new Stud(new Measurement(92), Stud.Dimension.TWO_BY_FOUR).material());

        MaterialList secondResult = new MaterialList().addMaterial(Nails.TEN_D, 6)
                .addMaterial(new Lumber(new Measurement(96), Stud.Dimension.TWO_BY_FOUR), 1);
        assertEquals(secondResult, new Stud(new Measurement(92, Measurement.Fraction.ELEVEN_SIXTEENTH), Stud.Dimension.TWO_BY_FOUR).material());
    }

    @Test
    public void studShouldBeEqualWhenOfTheSameLength() {
        assertEquals(new Stud(new Measurement(95), Lumber.Dimension.TWO_BY_FOUR),
                new Stud(new Measurement(95), Lumber.Dimension.TWO_BY_FOUR));
    }

    @Test
    public void studShouldCalculateHashCode() {
        int result = 1541510283;
        assertEquals(result, new Stud().hashCode());
    }

    @Test
    public void studShouldNotBeEqualWhenOfDifferentInstalledLength() {
        assertNotEquals(new Stud(), new Stud(new Measurement(92), Lumber.Dimension.TWO_BY_FOUR));
    }
}
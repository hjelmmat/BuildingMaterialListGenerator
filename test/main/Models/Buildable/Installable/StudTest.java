package main.Models.Buildable.Installable;

import main.Models.Buildable.Material.Lumber;
import main.Models.Buildable.Material.MaterialList;
import main.Models.Buildable.Material.Nail;
import main.Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudTest {
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    @Test
    public void defaultStudShouldHave8FtLength() {
        Measurement result = new Measurement(92, Measurement.Fraction.FIVE_EIGHTH);
        assertEquals(result, new Stud().installedLength);
    }

    @Test
    public void studShouldProduceMaterialList() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 6)
                .addMaterial(new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), this.dimension), 1);
        assertEquals(result, new Stud().material());
        assertEquals(result, new Stud(new Measurement(92), this.dimension).material());

        MaterialList secondResult = new MaterialList().addMaterial(Nail.TEN_D, 6)
                .addMaterial(new Lumber(new Measurement(96), this.dimension), 1);
        assertEquals(secondResult, new Stud(new Measurement(92, Measurement.Fraction.ELEVEN_SIXTEENTH), this.dimension).material());
    }

    @Test
    public void studShouldBeEqualWhenOfTheSameLength() {
        assertEquals(new Stud(new Measurement(95), this.dimension),
                new Stud(new Measurement(95), this.dimension));
    }

    @Test
    public void studShouldCalculateHashCode() {
        int result = 2045471944;
        assertEquals(result, new Stud().hashCode());
    }

    @Test
    public void studShouldNotBeEqualWhenOfDifferentInstalledLength() {
        assertNotEquals(new Stud(), new Stud(new Measurement(92), this.dimension));
    }
}
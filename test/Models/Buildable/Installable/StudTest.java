package Models.Buildable.Installable;

import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudTest {
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    @Test
    public void defaultStudShouldHave8FtLength() {
        MaterialList result = new MaterialList().addMaterial(new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), dimension), 1)
                .addMaterial(Nail.TEN_D, 6);
        MaterialList test = new Stud().materialList();
        String error = String.format("Expected %s, was %s", result.materials(), test.materials());
        assertEquals(result, test, error);
    }

    @Test
    public void studShouldProduceMaterialList() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 6)
                .addMaterial(new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), this.dimension), 1);
        assertEquals(result, new Stud().materialList());
        assertEquals(result, new Stud(new Measurement(92), this.dimension).materialList());

        MaterialList secondResult = new MaterialList().addMaterial(Nail.TEN_D, 6)
                .addMaterial(new Lumber(new Measurement(96), this.dimension), 1);
        assertEquals(secondResult, new Stud(new Measurement(92, Measurement.Fraction.ELEVEN_SIXTEENTH), this.dimension).materialList());
    }

    @Test
    public void studShouldBeEqualWhenOfTheSameLength() {
        assertEquals(new Stud(new Measurement(95), this.dimension),
                new Stud(new Measurement(95), this.dimension));
    }

    @Test
    public void studShouldCalculateHashCode() {
        int result = 1742707305;
        assertEquals(result, new Stud().hashCode());
    }

    @Test
    public void studShouldNotBeEqualWhenOfDifferentInstalledLength() {
        assertNotEquals(new Stud(), new Stud(new Measurement(92), this.dimension));
    }
}
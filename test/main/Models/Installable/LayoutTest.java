package main.Models.Installable;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Nail;
import main.Models.Measurement;
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
    public void layoutShouldReturnMaterials() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 12)
                .addMaterial(new Lumber(new Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2);
        assertEquals(result, this.basicLayout.material());
    }

    @Test
    public void layoutShouldDetermineEquals() {
        Layout testingLayout = new Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud);
        assertEquals(basicLayout, testingLayout);
    }

    @Test
    public void layoutShouldProvideHashCode() {
        int result = -1932215140;
        assertEquals(result, basicLayout.hashCode());
    }
}
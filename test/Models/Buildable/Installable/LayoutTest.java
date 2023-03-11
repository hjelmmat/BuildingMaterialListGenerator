package Models.Buildable.Installable;

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
    public void layoutShouldReturnMaterials() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 12)
                .addMaterial(new Lumber(new Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2);
        assertEquals(result, this.basicLayout.materialList());
    }

    @Test
    public void layoutShouldDetermineEquals() {
        Layout testingLayout = new Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud);
        assertEquals(basicLayout, testingLayout);
    }
}
package main.Models.Installable;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Nail;
import main.Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.TreeMap;

class LayoutTest {
    Stud defaultStud = new Stud();
    Measurement firstPosition = new Measurement(0);
    Measurement secondPosition = new Measurement(1, Measurement.Fraction.ONE_HALF);
    Layout basicLayout = new Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud);

    @Test
    public void layoutShouldAddAStud(){
        TreeMap<Measurement, Stud> result = new TreeMap<>();
        result.put(this.firstPosition, this.defaultStud);
        result.put(this.secondPosition, this.defaultStud);
        assertEquals(result, this.basicLayout);
    }

    @Test
    public void layoutShouldAddStudWithPositionCopy() {
        TreeMap<Measurement, Stud> result = new TreeMap<>();
        result.put(this.firstPosition, this.defaultStud);
        result.put(this.secondPosition, this.defaultStud);
        Measurement position = new Measurement(0);
        assertEquals(result, new Layout().addStudAt(position, this.defaultStud).
                addStudAt(position.add(this.secondPosition), this.defaultStud));
    }

    @Test
    public void layoutShouldReturnMaterials() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 12)
                .addMaterial(new Lumber(new Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2);
        assertEquals(result, this.basicLayout.material());
    }
}
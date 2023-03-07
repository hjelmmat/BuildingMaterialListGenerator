package main.Models.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MaterialListTest {
    @Test
    public void materialListShouldAddMembers() {
        HashMap<Material, Integer> result = new HashMap<Material, Integer>();
        Stud stud = new Stud(new Measurement(90), Stud.Dimension.TWO_BY_FOUR);
        result.put(stud, 1);
        assertEquals(result, new MaterialList().addMaterial(stud, 1));
    }

    @Test
    public void materialListShouldIncreaseQuantityWithNewMaterial() {
        HashMap<Material, Integer> result = new HashMap<Material, Integer>();
        Stud stud = new Stud(new Measurement(90), Stud.Dimension.TWO_BY_FOUR);
        result.put(stud, 2);
        assertEquals(result, new MaterialList().addMaterial(stud, 1).addMaterial(stud, 1));
    }

    @Test
    public void materialListShouldAddSeparateMaterialList() {
        HashMap<Material, Integer> result = new HashMap<Material, Integer>();
        Stud stud = new Stud(new Measurement(90), Stud.Dimension.TWO_BY_FOUR);
        result.put(stud, 2);
        MaterialList testable = new MaterialList().addMaterial(stud, 2);
        assertEquals(result, new MaterialList().addMaterials(testable));
    }
}
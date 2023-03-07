package main.Models.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MaterialListTest {
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    @Test
    public void materialListShouldAddMembers() {
        HashMap<Material, Integer> result = new HashMap<Material, Integer>();
        Lumber lumber = new Lumber(new Measurement(90), this.dimension);
        result.put(lumber, 1);
        assertEquals(result, new MaterialList().addMaterial(lumber, 1));
    }

    @Test
    public void materialListShouldIncreaseQuantityWithNewMaterial() {
        HashMap<Material, Integer> result = new HashMap<Material, Integer>();
        Lumber lumber = new Lumber(new Measurement(90), this.dimension);
        result.put(lumber, 2);
        assertEquals(result, new MaterialList().addMaterial(lumber, 1).addMaterial(lumber, 1));
    }

    @Test
    public void materialListShouldAddSeparateMaterialList() {
        HashMap<Material, Integer> result = new HashMap<Material, Integer>();
        Lumber Lumber = new Lumber(new Measurement(90), this.dimension);
        result.put(Lumber, 2);
        MaterialList testable = new MaterialList().addMaterial(Lumber, 2);
        assertEquals(result, new MaterialList().addMaterials(testable));
    }
}
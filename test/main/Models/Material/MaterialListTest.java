package main.Models.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class MaterialListTest {
    private static final Lumber lumber = new Lumber(new Measurement(90),  Lumber.Dimension.TWO_BY_FOUR);
    private static Vector<Vector<Object>> result;

    @BeforeAll
    static void setup() {
        result = new Vector<>();
        Vector<Object> firstElement = new Vector<>(List.of(lumber, 2));
        result.add(firstElement);
    }

    @Test
    public void materialListShouldIncreaseQuantityWithNewMaterial() {
        assertEquals(result, new MaterialList().addMaterial(lumber, 1).addMaterial(lumber, 1).asVector());
    }

    @Test
    public void materialListShouldAddSeparateMaterialList() {
        MaterialList testable = new MaterialList().addMaterial(lumber, 2);
        assertEquals(result, new MaterialList().addMaterials(testable).asVector());
    }

    @Test
    public void shouldReturnContentsAsVector() {
        assertEquals(result, new MaterialList().addMaterial(lumber, 2).asVector());
    }
}
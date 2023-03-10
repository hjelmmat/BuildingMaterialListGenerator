package main.Models.Buildable.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class MaterialListTest {
    private static final Lumber lumber = new Lumber(new Measurement(48),  Lumber.Dimension.TWO_BY_FOUR);
    private static Vector<Vector<Object>> result;

    @BeforeEach
    void setup() {
        result = new Vector<>();
        result.add(new Vector<>(List.of("Material", "Quantity")));
        result.add(new Vector<>(List.of(lumber.toString(), "2")));
    }

    @Test
    public void materialListShouldIncreaseQuantityWithNewMaterial() {
        assertEquals(result, new MaterialList().addMaterial(lumber, 1).addMaterial(lumber, 1).materials());
    }

    @Test
    public void shouldReturnContentsVector() {
        MaterialList test = new MaterialList().addMaterial(lumber, 2);
        assertEquals(result, test.materials());
        result.add(new Vector<>(List.of("10d nails", "2")));
        assertEquals(result, test.addMaterial(Nail.TEN_D, 2).materials());
    }

    @Test
    public void shouldReturnEqual() {
        MaterialList one = new MaterialList().addMaterial(lumber, 1);
        MaterialList two = new MaterialList().addMaterial(lumber, 1);
        assertEquals(one, two);
    }

    @Test
    public void shouldCreateHashCode() {
        int result = -1964873812;
        assertEquals(result, new MaterialList().addMaterial(lumber, 1).hashCode());
    }
}
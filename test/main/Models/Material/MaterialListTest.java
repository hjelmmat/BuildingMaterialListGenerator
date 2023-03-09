package main.Models.Material;

import main.Models.Measurement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class MaterialListTest {
    private static final Lumber lumber = new Lumber(new Measurement(90),  Lumber.Dimension.TWO_BY_FOUR);
    private static DefaultTableModel result;

    @BeforeAll
    static void setup() {
        Vector<String> columnHeaders = new Vector<>(List.of("Material", "Quantity"));
        Vector<Object> firstElement = new Vector<>(List.of(lumber, 2));
        Vector<Vector<Object>> data = new Vector<>();
        data.add(firstElement);
        result = new DefaultTableModel(data, columnHeaders);
    }

    private void validateTableModel(DefaultTableModel testModel) {
        assertEquals(result.getDataVector(), testModel.getDataVector());
        assertEquals(result.getColumnCount(), result.getColumnCount());
        for (int i=0; i <= testModel.getColumnCount(); i++) {
            assertEquals(result.getColumnName(i), testModel.getColumnName(i));
        }
    }

    @Test
    public void materialListShouldIncreaseQuantityWithNewMaterial() {
        this.validateTableModel(new MaterialList().addMaterial(lumber, 1).addMaterial(lumber, 1).asTableModel());
    }

    @Test
    public void materialListShouldAddSeparateMaterialList() {
        this.validateTableModel(new MaterialList().addMaterial(lumber, 2).asTableModel());
    }

    @Test
    public void shouldReturnContentsAsTableModel() {
        this.validateTableModel(new MaterialList().addMaterial(lumber, 2).asTableModel());
    }

    @Test
    public void shouldReturnEqual() {
        MaterialList one = new MaterialList().addMaterial(lumber, 1);
        MaterialList two = new MaterialList().addMaterial(lumber, 1);
        assertEquals(one, two);
    }

    @Test
    public void shouldCreateHashCode() {
        int result = 1134855128;
        assertEquals(result, new MaterialList().addMaterial(lumber, 1).hashCode());
    }
}
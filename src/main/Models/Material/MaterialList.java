package main.Models.Material;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * A container for the Material needed to build something
 */
public class MaterialList {
    private final HashMap<Material, Integer> map;
    private final static Vector<String> keyValueProperties =  new Vector<>(List.of("Material", "Quantity"));

    /**
     * Create a MaterialList to store material and quantities.
     */
    public MaterialList() {
        this.map = new HashMap<>();
    }

    /**
     *
     * @param type - the type of material to add
     * @param quantity - the number of materials to add. This will add to any materials of type 'type' if already present
     * @return - this updated MaterialList
     */
    public MaterialList addMaterial(Material type, int quantity) {
        this.map.merge(type, quantity, Integer::sum);
        return this;
    }

    /**
     * A conveience to loop through an already existing MaterialList and add it to this
     * @param materials - materials to add to this
     * @return - this updated MaterialList
     */
    public MaterialList addMaterials(MaterialList materials) {
        materials.map.forEach(this::addMaterial);
        return this;
    }

    /**
     *
     * @return - This material list as a DefaultTableModel
     */
    public DefaultTableModel asTableModel() {
        Vector<Vector<Object>> data = new Vector<>();
        this.map.forEach((k,v) -> data.add(new Vector<>(List.of(k, v))));
        return new DefaultTableModel(data, keyValueProperties);
    }

    /**
     *
     * @param obj - the object to be compared
     * @return Indication if the two objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MaterialList m)) {
            return false;
        }
        return this.map.equals(m.map);
    }

    /**
     *
     * @return The hash code for this MaterialList
     */
    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}

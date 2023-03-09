package main.Models.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * A container for the Material needed to build something
 */
public class MaterialList {
    private final HashMap<Material, Integer> map;

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

    public Vector<Vector<Object>> asVector() {
        Vector<Vector<Object>> result = new Vector<>();
        this.map.forEach((k,v) -> result.add(new Vector<>(List.of(k, v))));
        return result;
    }
}

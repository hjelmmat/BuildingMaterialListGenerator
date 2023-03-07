package main.Models.Material;

import java.util.HashMap;

/**
 * A hashmap containing the Material needed to build something
 */
public class MaterialList extends HashMap<Material, Integer> {
    /**
     *
     * @param type - the type of material to add
     * @param quantity - the number of materials to add. This will add to any materials of type 'type' if already present
     * @return - this updated MaterialList
     */
    public MaterialList addMaterial(Material type, int quantity) {
        this.merge(type, quantity, Integer::sum);
        return this;
    }

    /**
     * A conveience to loop through an already existing MaterialList and add it to this
     * @param materials - materials to add to this
     * @return - this updated MaterialList
     */
    public MaterialList addMaterials(MaterialList materials) {
        materials.forEach(this::addMaterial);
        return this;
    }
}

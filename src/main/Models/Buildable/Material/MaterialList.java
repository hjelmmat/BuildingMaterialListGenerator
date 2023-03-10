package main.Models.Buildable.Material;

import java.util.*;

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

    /**
     *
     * @return a Vector in which the first element describes the elements of the rest of the elements as:
     * ["Material", "Quantity"]
     * and the rest of the elements are the [Material, Quantity] of the list represented as Strings
     */
    public Vector<Vector<String>> materials() {
        Vector<Vector<String>> result = new Vector<>();
        result.add(keyValueProperties);
        Vector<Vector<String>> nails = new Vector<>();
        Vector<Vector<String>> lumber = new Vector<>();
        for (Material key : this.map.keySet()) {
            Vector<String> keyValue = new Vector<>(List.of(key.toString(), this.map.get(key).toString()));
            Class<? extends Material> keyClass = key.getClass();
            if (keyClass == Nail.class) {
                nails.add(keyValue);
            }
            else if (keyClass == Lumber.class) {
                lumber.add(keyValue);
            }
        }
        nails.sort(Comparator.comparing(v -> v.get(0)));
        lumber.sort(Comparator.comparing(v -> v.get(0)));
        result.addAll(lumber);
        result.addAll(nails);
        return result;
    }
}

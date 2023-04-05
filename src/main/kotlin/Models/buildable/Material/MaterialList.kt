package models.buildable.material

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.listOf

/**
 * A container for the Material needed to build something
 * Create an empty MaterialList to store material and quantities.
 */
class MaterialList {
    private val map: HashMap<Material, Int> = HashMap()

    /**
     *
     * @param type - the type of material to add
     * @param quantity - the number of materials to add. This will add to any materials of type 'type' if already present
     * @return - this updated MaterialList
     */
    fun addMaterial(type: Material, quantity: Int): MaterialList {
        map.merge(type, quantity) { a: Int, b: Int -> Integer.sum(a, b) }
        return this
    }

    /**
     * A conveience to loop through an already existing MaterialList and add it to this
     * @param materials - materials to add to this
     * @return - this updated MaterialList
     */
    fun addMaterials(materials: MaterialList): MaterialList {
        materials.map.forEach { (type: Material, quantity: Int) -> addMaterial(type, quantity) }
        return this
    }

    /**
     *
     * @param other - the object to be compared
     * @return Indication if the two objects are equal
     */
    override fun equals(other: Any?) = (other === this)
            || other is MaterialList && this.map == other.map

    /**
     *
     * @return The hash code for this MaterialList
     */
    override fun hashCode(): Int {
        return map.hashCode()
    }

    /**
     *
     * @return a Vector in which the first element describes the elements of the rest of the elements as:
     * ["Material", "Quantity"]
     * and the rest of the elements are the [Material, Quantity] of the list represented as Strings
     */
    fun materials(): Vector<Vector<String>> {
        val result = Vector<Vector<String>>()
        val nails = Vector<Vector<String>>()
        val lumber = Vector<Vector<String>>()
        map.forEach { (material: Material, quantity: Int) ->
            when (material) {
                is Nail -> nails.add(Vector(listOf(material.toString(), quantity.toString())))
                is Lumber -> lumber.add(Vector(listOf(material.toString(), quantity.toString())))
            }
        }
        nails.sortBy { it[0] }
        lumber.sortBy { it[0] }
        result.addAll(lumber)
        result.addAll(nails)
        return result
    }
}
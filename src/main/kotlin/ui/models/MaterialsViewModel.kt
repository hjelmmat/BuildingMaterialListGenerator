package ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*


class Material(val material: String, val quantity: String) {
    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is Material && material == other.material && quantity == other.quantity)
    }

    override fun hashCode(): Int {
        return "$material$quantity".hashCode()
    }
}

class MaterialsViewModel {
    val materialTitle = "Material"
    val quantityTitle = "Quantity"
    var data by mutableStateOf<List<Material>>(emptyList())
        private set

    fun updateData(newData: Vector<Vector<String>>): MaterialsViewModel {
        data = List(newData.size) {
            val value = newData[it]
            Material(value[0], value[1])
        }
        return this
    }
}

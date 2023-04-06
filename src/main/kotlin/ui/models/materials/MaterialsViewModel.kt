package ui.models.materials

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*


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

package ui.models.materials

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*
import kotlin.collections.HashMap


class MaterialsViewModel {
    val materialTitle = "Material"
    val quantityTitle = "Quantity"
    var data by mutableStateOf<List<Material>>(emptyList())
        private set

    fun updateData(newData: HashMap<String, Vector<Vector<String>>>): MaterialsViewModel {
        val temp = Vector<Material>()
        newData.forEach() {
            it.value.forEach() { material -> temp.add(Material(material[0], material[1])) }
        }
        data = temp.toList()
        return this
    }
}

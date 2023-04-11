package ui.models.materials

import java.util.Vector
import kotlin.test.*

class MaterialsViewModelTest {
    @Test
    fun shouldAddData() {
        val test = MaterialsViewModel()
        val data: HashMap<String, Vector<Vector<String>>> = HashMap()
        val nails = Vector<Vector<String>>()
        nails.add(Vector(listOf("", "")))
        data["nails"] = nails
        assertEquals(mutableListOf(Material("", "")), test.updateData(data).data)
    }
}
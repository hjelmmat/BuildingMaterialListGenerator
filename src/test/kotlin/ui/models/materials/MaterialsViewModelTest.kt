package ui.models.materials

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Vector

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
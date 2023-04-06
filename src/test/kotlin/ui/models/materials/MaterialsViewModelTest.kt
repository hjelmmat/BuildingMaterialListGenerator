package ui.models.materials

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ui.models.materials.Material
import ui.models.materials.MaterialsViewModel
import java.util.Vector

class MaterialsViewModelTest {
    @Test
    fun shouldAddData() {
        val test = MaterialsViewModel()
        val data: Vector<Vector<String>> = Vector()
        data.add(Vector(listOf("", "")))
        assertEquals(mutableListOf(Material("", "")), test.updateData(data).data)
    }
}
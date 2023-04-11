package ui.models.graphics

import androidx.compose.ui.geometry.*
import java.util.*
import kotlin.collections.HashMap

import kotlin.test.*

class GraphicsViewModelTest {
    @Test
    fun shouldUpdateData() {
        val test = GraphicsViewModel()
        val data = HashMap<String, Vector<Vector<Int>>>()
        test.updateData(data)
        assertEquals(emptyList<Graphic>(), test.data)
        val rectangles = Vector<Vector<Int>>()
        rectangles.add(Vector<Int>(listOf(0, 0, 1, 1)))
        data["rectangles"] = rectangles
        test.updateData(data)
        assertEquals(
            listOf(Graphic(Offset(0F, 0F), Size(1F, 1F))),
            test.data
        )
    }
}
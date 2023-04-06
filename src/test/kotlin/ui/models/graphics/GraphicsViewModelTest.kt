package ui.models.graphics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ui.models.graphics.Graphic
import ui.models.graphics.GraphicsViewModel
import java.util.Vector

class GraphicsViewModelTest {
    @Test
    fun shouldUpdateData() {
        val test = GraphicsViewModel()
        val data = Vector<Vector<Vector<Int>>>()
        val dataOne = Vector<Vector<Int>>()
        val dataTwo = Vector<Vector<Int>>()
        data.add(dataOne)
        data.add(dataTwo)
        test.updateData(data)
        assertEquals(emptyList<Graphic>(), test.data)
        dataTwo.add(Vector<Int>(listOf(0, 0, 1, 1)))
        test.updateData(data)
        assertEquals(
            listOf(Graphic(Offset(0F, 0F), Size(1F, 1F))),
            test.data
        )
    }
}
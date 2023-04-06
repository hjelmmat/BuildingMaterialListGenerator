package ui.models.graphics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import java.util.*


class GraphicsViewModel {
    var data by mutableStateOf<List<Graphic>>(emptyList())
        private set

    fun updateData(newData: Vector<Vector<Vector<Int>>>): GraphicsViewModel {
        val rectangles = newData[1]
        data = List(rectangles.size) {
            val value: Vector<Int> = rectangles[it]
            Graphic(Offset(value[0].toFloat(), value[1].toFloat()), Size(value[2].toFloat(), value[3].toFloat()))
        }
        return this
    }
}

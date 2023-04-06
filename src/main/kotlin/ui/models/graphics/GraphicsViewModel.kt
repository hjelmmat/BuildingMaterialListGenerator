package ui.models.graphics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import java.util.*
import kotlin.collections.HashMap


class GraphicsViewModel {
    var data by mutableStateOf<List<Graphic>>(emptyList())
        private set

    fun updateData(newData: HashMap<String, Vector<Vector<Int>>>): GraphicsViewModel {
        newData["rectangles"]?.let { rectangleList ->
            data = List(rectangleList.size) {
                val value: Vector<Int> = rectangleList[it]
                Graphic(Offset(value[0].toFloat(), value[1].toFloat()), Size(value[2].toFloat(), value[3].toFloat()))
            }
        }
        return this
    }
}

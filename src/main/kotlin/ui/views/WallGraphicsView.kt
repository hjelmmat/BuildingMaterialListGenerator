package ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import ui.models.GraphicsViewModel

@Composable
fun wallGraphicsView(model: GraphicsViewModel) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        model.data.forEach {
            drawRect(
                color = Color.Black,
                topLeft = it.offset,
                size = it.size,
                style = Stroke()
            )
        }
    }
}
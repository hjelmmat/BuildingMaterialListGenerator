package ui.views.wall

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.models.wall.WallContentsViewModel
import ui.views.materialsView

@Composable
fun wallContentsView(model: WallContentsViewModel) {
    Row(modifier = Modifier.padding(16.dp)) {
        materialsView(model.materials)
        wallGraphicsView(model.graphics)
    }
}

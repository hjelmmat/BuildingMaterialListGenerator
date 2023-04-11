package ui.views.wall

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.models.wall.WallDialogModel
import ui.views.materialsView

@Composable
fun wallDialog(model: WallDialogModel) {
    Row {
        Column(modifier = Modifier.padding(16.dp)) {
            createWallView(model.createWallModel)
            addDoorView(model.doorModel)
            addWindowView(model.windowModel)
            materialsView(model.materials)
        }
        wallGraphicsView(model.graphics) // TODO: Make scrollable or scalable so you can see the whole output
    }
}
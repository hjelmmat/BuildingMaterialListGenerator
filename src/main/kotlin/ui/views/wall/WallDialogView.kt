package ui.views.wall

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import ui.models.wall.WallDialogModel

@Composable
fun wallDialog(model: WallDialogModel) {
    Row {
        Column {
            createWallView(model.createWallModel)
            addDoorView(model.doorModel)
        }
        wallContentsView(model.wallContentsModel) // TODO: Make scrollable or scalable so you can see the whole output
    }
}
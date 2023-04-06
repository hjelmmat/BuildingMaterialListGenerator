package ui.views.wall

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import ui.models.wall.AddDoorViewModel
import ui.views.buttonWithErrorView
import ui.views.measurementView

@Composable
fun addDoorView(model: AddDoorViewModel) {
    Row {
        measurementView(model.location)
        buttonWithErrorView(model.addDoorButton)
    }
}
package ui.views.wall

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.models.wall.AddDoorViewModel
import ui.views.buttonWithErrorView
import ui.views.measurementView

@Composable
fun addDoorView(model: AddDoorViewModel) {
    Column {
        Text("Add A Door", modifier = Modifier.align(Alignment.CenterHorizontally))
        measurementView(model.location)
        buttonWithErrorView(model.addDoorButton)
    }
}
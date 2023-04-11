package ui.views.wall

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.models.wall.AddWindowViewModel
import ui.views.buttonWithErrorView
import ui.views.measurementView

@Composable
fun addWindowView(model: AddWindowViewModel) {
    Column {
        Text("Add A Window", modifier = Modifier.align(Alignment.CenterHorizontally))
        measurementView(model.location)
        measurementView(model.width)
        measurementView(model.height)
        measurementView(model.heightToBottomOfWindow)
        buttonWithErrorView(model.addWindowButton)
    }
}
package ui.views.wall

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.models.wall.CreateWallViewModel
import ui.views.buttonWithErrorView
import ui.views.measurementView

@Composable
fun createWallView(viewModel: CreateWallViewModel) {
    Column {
        Text("Create A Wall", modifier = Modifier.align(Alignment.CenterHorizontally))
        measurementView(viewModel.heightMeasurement)
        measurementView(viewModel.lengthMeasurement)
        buttonWithErrorView(viewModel.buttonWithError)
    }
}

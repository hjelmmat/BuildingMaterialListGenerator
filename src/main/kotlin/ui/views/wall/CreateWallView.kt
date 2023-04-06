package ui.views.wall

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import ui.models.wall.CreateWallViewModel
import ui.views.buttonWithErrorView
import ui.views.measurementView

@Composable
fun createWallView(viewModel: CreateWallViewModel) {
    Row {
        measurementView(viewModel.heightMeasurement)
        measurementView(viewModel.lengthMeasurement)
        buttonWithErrorView(viewModel.buttonWithError)
    }
}

package ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.models.CreateWallViewModel

@Composable
fun createWallView(viewModel: CreateWallViewModel) {
    Column {
        Row {
            measurementView(viewModel.heightMeasurement)
            measurementView(viewModel.lengthMeasurement)
            Button(
                onClick = { viewModel.calculateMaterials() },
                enabled = viewModel.validMeasurements(),
                content = {
                    Text("Calculate")
                }
            )
            materialsView(viewModel.materials)
        }
        Row(modifier = Modifier.padding(16.dp)) {
            wallGraphicsView(viewModel.graphics)
        }
    }
}
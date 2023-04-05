package ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.models.MaterialsViewModel

@Composable
fun RowScope.TableCell(text: String) {
    Text(
        text = text,
        modifier = Modifier.border(1.dp, Color.Black).padding(8.dp).weight(.5f)
    )
}

@Composable
fun materialsView(model: MaterialsViewModel) {
    LazyColumn(Modifier.padding(16.dp).size(250.dp, 150.dp)) {
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(model.materialTitle)
                TableCell(model.quantityTitle)
            }
        }
        items(model.data) {
            Row {
                TableCell(it.material)
                TableCell(it.quantity)
            }
        }
    }
}
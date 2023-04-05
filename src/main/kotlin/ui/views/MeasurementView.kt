package ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import ui.models.MeasurementViewModel

@Composable
fun measurementView(measurementModel: MeasurementViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Row() {
        Column {
            TextField(
                value = measurementModel.integerValue,
                label = { Text(measurementModel.name) },
                onValueChange = { measurementModel.integerValue = it },
                singleLine = true,
                isError = !measurementModel.validValue(),
                modifier = Modifier.width(100.dp)
            )
            if (!measurementModel.validValue()) {
                Text(
                    text = measurementModel.errorMessage,
                    modifier = Modifier.width(95.dp).padding(5.dp),
                    color = MaterialTheme.colors.error,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
        Box() {
            TextField(
                value = measurementModel.fractionValue.toString(),
                label = { Text("Fraction") },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Open Options"
                        )
                    }
                },
                modifier = Modifier.width(125.dp)
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                measurementModel.fractionOptions.forEachIndexed { _, itemValue ->
                    DropdownMenuItem(
                        onClick = {
                            measurementModel.fractionValue = itemValue
                            expanded = false
                        },
                        enabled = true
                    ) {
                        Text(text = itemValue.toString())
                    }
                }
            }
        }
    }
}


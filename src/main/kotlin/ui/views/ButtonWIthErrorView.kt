package ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.models.ButtonWithErrorViewModel

@Composable
fun buttonWithErrorView(model: ButtonWithErrorViewModel) {
    Row {
        Button(
            onClick = model.buttonOnClick,
            enabled = model.buttonEnabled(),
            content = { Text(model.buttonText) }
        )
        if (model.shouldDisplayError()) {
            Text(
                text = model.errorMessage(),
                modifier = Modifier.width(100.dp).padding(5.dp),
                color = MaterialTheme.colors.error,
                fontSize = 10.sp,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}
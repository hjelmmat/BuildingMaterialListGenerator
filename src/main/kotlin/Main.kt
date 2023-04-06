import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.models.wall.WallDialogModel
import ui.views.wall.wallDialog


fun main() = application {
    val wallModel = remember { WallDialogModel() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1250.dp, height = 1000.dp)
    ) {
        MaterialTheme {
            wallDialog(wallModel)
        }
    }
}
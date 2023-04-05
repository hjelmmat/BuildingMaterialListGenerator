import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.models.CreateWallViewModel
import ui.views.createWallView


fun main() = application {
    val createWallViewModel = remember { CreateWallViewModel() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 1000.dp, height = 500.dp)
    ) {
        MaterialTheme {
            Row {
                createWallView(createWallViewModel)
            }
        }
    }
}
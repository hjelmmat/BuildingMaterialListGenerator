package ui.models.wall

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import models.Measurement
import ui.models.*

class AddDoorViewModel(private val addDoorMethod: (Measurement) -> Unit) {
    var addDoorError by mutableStateOf("")
        private set
    val location = MeasurementViewModel("Location")
    val addDoorButton = ButtonWithErrorViewModel(
        buttonOnClick = ::addDoor,
        buttonEnabled = location::isValidValue,
        buttonText = "Add Door",
        shouldDisplayError = { addDoorError != "" },
        errorMessage = { addDoorError }
    )

    fun addDoor() {
        if (location.isValidValue()) {
            addDoorError = try {
                addDoorMethod(location.asMeasurement())
                ""
            } catch (e: IllegalArgumentException) {
                e.message.toString()
            }
        }
    }
}

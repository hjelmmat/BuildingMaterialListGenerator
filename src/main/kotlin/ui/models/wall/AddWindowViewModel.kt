package ui.models.wall

import androidx.compose.runtime.*
import models.Measurement
import ui.models.ButtonWithErrorViewModel
import ui.models.MeasurementViewModel

class AddWindowViewModel(private val addWindowMethod: (Measurement, Measurement, Measurement, Measurement) -> Unit) {
    var addWindowError by mutableStateOf("")
        private set
    val location = MeasurementViewModel("Location")
    val heightToBottomOfWindow = MeasurementViewModel("Height To Bottom")
    val width = MeasurementViewModel("Width")
    val height = MeasurementViewModel("Height")
    val addWindowButton = ButtonWithErrorViewModel(
        buttonOnClick = ::addWindow,
        buttonEnabled = ::isValid,
        buttonText = "Add",
        shouldDisplayError = { addWindowError != "" },
        errorMessage = { addWindowError }
    )

    private fun isValid(): Boolean {
        return location.isValidValue() &&
                width.isValidValue() &&
                height.isValidValue() &&
                heightToBottomOfWindow.isValidValue()
    }

    fun addWindow() {
        if (isValid()) {
            addWindowError = try {
                addWindowMethod(
                    location.asMeasurement(),
                    heightToBottomOfWindow.asMeasurement(),
                    width.asMeasurement(),
                    height.asMeasurement()
                )
                ""
            } catch (e: IllegalArgumentException) {
                e.message.toString()
            }
        }

    }
}

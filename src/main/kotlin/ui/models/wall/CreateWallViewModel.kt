package ui.models.wall

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import models.Measurement
import models.buildable.Wall
import ui.models.ButtonWithErrorViewModel
import ui.models.MeasurementViewModel


class CreateWallViewModel(private val addWallMethod: (Measurement, Measurement) -> Unit) {
    var addWallError by mutableStateOf("")
        private set
    val heightMeasurement = MeasurementViewModel("Height", Wall.minimumHeight)
    val lengthMeasurement = MeasurementViewModel("Width", Wall.minimumWidth)
    val buttonWithError = ButtonWithErrorViewModel(
        buttonOnClick = ::addWall,
        buttonEnabled = ::isValidMeasurements,
        buttonText = "Create",
        shouldDisplayError = { addWallError != "" },
        errorMessage = { addWallError }
    )

    fun isValidMeasurements(): Boolean {
        return heightMeasurement.isValidValue() && lengthMeasurement.isValidValue()
    }

    fun addWall() {
        if (isValidMeasurements()) {
            addWallError = try {
                addWallMethod(lengthMeasurement.asMeasurement(), heightMeasurement.asMeasurement())
                ""
            } catch (e: IllegalArgumentException) {
                e.message.toString()
            }
        }
    }
}
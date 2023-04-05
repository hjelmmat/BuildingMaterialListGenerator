package ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import models.Measurement
import kotlin.jvm.Throws

class MeasurementViewModel(val name: String, private val minimumValue: Measurement = Measurement(0)) {
    val errorMessage = "$name >= $minimumValue"
    val fractionOptions = Measurement.Fraction.values()
    var integerValue: String by mutableStateOf("0")
    var fractionValue: Measurement.Fraction by mutableStateOf(Measurement.Fraction.ZERO)

    fun validValue(): Boolean {
        return try {
            Measurement(integerValue.toInt(), fractionValue) >= minimumValue
        } catch (e: NumberFormatException) {
            false
        } catch (e: Measurement.InvalidMeasurementException) {
            false
        }
    }

    @Throws(Measurement.InvalidMeasurementException::class)
    fun asMeasurement(): Measurement {
        return if (validValue()) {
            Measurement(integerValue.toInt(), fractionValue)
        } else {
            throw (Measurement.InvalidMeasurementException("Measurement is smaller than minimum value $minimumValue"))
        }
    }
}

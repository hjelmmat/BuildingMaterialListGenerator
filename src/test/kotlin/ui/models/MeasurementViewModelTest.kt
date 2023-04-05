package ui.models

import models.Measurement
import kotlin.test.Test
import kotlin.test.*

class MeasurementViewModelTest {
    @Test
    fun shouldValidateIntegerValue() {
        val test = MeasurementViewModel("Test")
        test.integerValue = "-1"
        assertEquals(false, test.validValue())
        test.integerValue = "0"
        assertEquals(true, test.validValue())
        test.integerValue = "1"
        assertEquals(true, test.validValue())
        test.integerValue = "a"
        assertEquals(false, test.validValue())
        test.integerValue = "1.1"
        assertEquals(false, test.validValue())
    }

    @Test
    fun shouldBeGreaterThanMinimumValue() {
        val test = MeasurementViewModel("Test", Measurement(5))
        assertEquals(test.errorMessage, "Test >= 5\"")
        assertEquals(false, test.validValue())
        test.integerValue = "4"
        test.fractionValue = Measurement.Fraction.FIFTEEN_SIXTEENTH
        assertEquals(false, test.validValue())
        test.integerValue = "5"
        test.fractionValue = Measurement.Fraction.ZERO
        assertEquals(true, test.validValue())
    }

    @Test
    fun shouldReturnAsMeasurement() {
        val test = MeasurementViewModel("Test", Measurement(5))
        val error = assertFailsWith<Measurement.InvalidMeasurementException> { test.asMeasurement() }
        assertEquals(error.message, "Measurement is smaller than minimum value 5\"")
        test.integerValue = "10"
        assertEquals(Measurement(10), test.asMeasurement())
    }
}

package ui.models.wall

import models.Measurement
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CreateWallViewModelTest {
    private val errorMessage = "You done messed up"

    private fun validWall(a: Measurement, b: Measurement) {}

    @Test
    fun shouldValidateMeasurements() {
        val test = CreateWallViewModel(::validWall)
        assertEquals(false, test.isValidMeasurements())
        test.heightMeasurement.integerValue = "10"
        assertEquals(false, test.isValidMeasurements())
        test.lengthMeasurement.integerValue = "5"
        assertEquals(true, test.isValidMeasurements())
    }

    @Test
    fun shouldCallMethodWhenAddingWall() {
        var result = Measurement(0)
        fun test(a: Measurement, b: Measurement) {
            result = a.add(b)
        }

        val test = CreateWallViewModel(::test)
        test.addWall()
        assertEquals(Measurement(0), result)

        test.heightMeasurement.integerValue = "10"
        test.addWall()
        assertEquals(Measurement(0), result)

        test.lengthMeasurement.integerValue = "5"
        test.addWall()
        assertEquals(Measurement(15), result)
    }

    @Test
    fun whenWallFailsToCreateShouldAddError() {
        var shouldPass = false
        fun changingFun(a: Measurement, b: Measurement) {
            if (!shouldPass) {
                throw IllegalArgumentException(errorMessage)
            }
        }

        val test = CreateWallViewModel(::changingFun)
        test.lengthMeasurement.integerValue = "10"
        test.heightMeasurement.integerValue = "15"
        assertEquals("", test.addWallError)
        test.addWall()
        assertEquals(errorMessage, test.addWallError)
        shouldPass = true
        test.addWall()
        assertEquals("", test.addWallError)
    }
}


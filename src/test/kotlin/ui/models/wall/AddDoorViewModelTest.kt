package ui.models.wall

import models.Measurement
import kotlin.test.Test
import kotlin.test.assertEquals

class AddDoorViewModelTest {
    @Test
    fun shouldAddDoor() {
        var result = Measurement(0)
        val testFun = { it: Measurement -> result = it }
        val test = AddDoorViewModel(testFun)
        test.addDoor()
        assertEquals(Measurement(0), result)
        test.location.integerValue = "10"
        test.addDoor()
        assertEquals(Measurement(10), result)
    }

    @Test
    fun whenLocationFailsShouldHaveErrorMessage() {
        val errorMessage = "Failed!"
        var shouldPass = false
        fun changingFun(a: Measurement) {
            if (!shouldPass) {
                throw IllegalArgumentException(errorMessage)
            }
        }

        val test = AddDoorViewModel(::changingFun)
        assertEquals("", test.addDoorError)

        test.addDoor()
        assertEquals(errorMessage, test.addDoorError)

        shouldPass = true
        test.addDoor()
        assertEquals("", test.addDoorError)
    }
}
package ui.models.wall

import models.Measurement
import kotlin.test.*

class AddWindowViewModelTest {
    @Test
    fun shouldAddWindow() {
        var location = Measurement(0)
        var heightToBW = Measurement(0)
        var width = Measurement(0)
        var height = Measurement(0)
        fun testFun(a: Measurement, b: Measurement, c: Measurement, d: Measurement) {
            location = a
            heightToBW = b
            width = c
            height = d
        }

        val result = listOf(location, heightToBW, width, height)
        val test = AddWindowViewModel(::testFun)
        test.addWindow()
        assertContentEquals(
            listOf(Measurement(0), Measurement(0), Measurement(0), Measurement(0)),
            result
        )
        test.location.integerValue = "1"
        test.heightToBottomOfWindow.integerValue = "2"
        test.width.integerValue = "3"
        test.height.integerValue = "4"
        test.addWindow()
        val secondResult = listOf(location, heightToBW, width, height)
        assertEquals(
            listOf(Measurement(1), Measurement(2), Measurement(3), Measurement(4)),
            secondResult
        )
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
        kotlin.test.assertEquals("", test.addDoorError)

        test.addDoor()
        kotlin.test.assertEquals(errorMessage, test.addDoorError)

        shouldPass = true
        test.addDoor()
        kotlin.test.assertEquals("", test.addDoorError)
    }
}
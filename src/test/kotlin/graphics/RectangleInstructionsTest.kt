package graphics

import models.Measurement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class RectangleInstructionsTest {
    var zero = Measurement(0)
    private var one = Measurement(1)
    private var ten = Measurement(10)

    @Test
    fun shouldImplementDrawingInstructions() {
        val one = Measurement(1).numberOfPixels
        val result = Vector(Vector(listOf(one, one, one, one)))
        val oneM = Measurement(1)
        Assertions.assertEquals(result, RectangleInstructions(oneM, oneM, oneM, oneM).drawingInstructions())
    }

    @Test
    fun shouldShift() {
        val result = Vector(listOf(ten.numberOfPixels, ten.numberOfPixels, one.numberOfPixels, one.numberOfPixels))
        val test = RectangleInstructions(zero, zero, one, one)
        Assertions.assertNotEquals(result, test.drawingInstructions())
        Assertions.assertEquals(result, test.shift(ten, ten).drawingInstructions())
        Assertions.assertEquals(result, test.shift(ten, ten).drawingInstructions())
    }
}
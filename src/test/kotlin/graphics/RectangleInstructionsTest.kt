package graphics

import models.Measurement
import kotlin.test.*
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
        assertEquals(result, RectangleInstructions(oneM, oneM, oneM, oneM).drawingInstructions())
    }

    @Test
    fun shouldShift() {
        val result = Vector(listOf(ten.numberOfPixels, ten.numberOfPixels, one.numberOfPixels, one.numberOfPixels))
        val test = RectangleInstructions(zero, zero, one, one)
        assertNotEquals(result, test.drawingInstructions())
        assertEquals(result, test.shift(ten, ten).drawingInstructions())
        assertEquals(result, test.shift(ten, ten).drawingInstructions())
    }
}
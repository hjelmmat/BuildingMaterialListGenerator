package graphics

import models.Measurement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class LineInstructionsTest {
    @Test
    fun shouldImplementDrawingInstructions() {
        val result = Vector(
            Vector(
                listOf(
                    Measurement(1).numberOfPixels, Measurement(2).numberOfPixels,
                    Measurement(3).numberOfPixels,
                    Measurement(4).numberOfPixels,
                ),
            ),
        )
        Assertions.assertEquals(
            result,
            LineInstructions(Measurement(1), Measurement(2), Measurement(3), Measurement(4)).drawingInstructions()
        )
    }

    @Test
    fun shouldShift() {
        val zero = Measurement(0)
        val five = Measurement(5)
        val ten = Measurement(10)
        val result = Vector(listOf(five.numberOfPixels, ten.numberOfPixels, five.numberOfPixels, ten.numberOfPixels))
        val test = LineInstructions(zero, zero, zero, zero)
        Assertions.assertNotEquals(result, test.drawingInstructions())
        Assertions.assertEquals(result, test.shift(five, ten).drawingInstructions())
        Assertions.assertEquals(result, test.shift(five, ten).drawingInstructions())
    }
}
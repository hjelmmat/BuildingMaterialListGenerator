package graphics

import models.Measurement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class GraphicsListTest {
    var zero = Measurement(0)
    private var ten = Measurement(10)

    @Test
    fun shouldAddLines() {
        val result = Vector<Vector<Vector<Int>>>()
        val lines = Vector<Vector<Int>>()
        val line = Vector(listOf(0, 0, ten.numberOfPixels, ten.numberOfPixels))
        lines.add(line)
        lines.add(line)
        result.add(lines)
        result.add(Vector())
        val instruction = LineInstructions(zero, zero, ten, ten)
        Assertions.assertEquals(
            result,
            GraphicsList().addGraphic(instruction).addGraphic(instruction).drawingInstructions()
        )
    }

    @Test
    fun shouldAddRectangles() {
        val result = Vector<Vector<Vector<Int>>>()
        result.add(Vector())
        val rectangles = Vector<Vector<Int>>()
        val rectangle = Vector(listOf(0, 0, ten.numberOfPixels, ten.numberOfPixels))
        rectangles.add(rectangle)
        result.add(rectangles)
        Assertions.assertEquals(
            result,
            GraphicsList().addGraphic(RectangleInstructions(zero, zero, ten, ten)).drawingInstructions()
        )
    }

    @Test
    fun shouldAddGraphicsList() {
        val result = GraphicsList().addGraphic(LineInstructions(zero, zero, zero, zero))
        Assertions.assertEquals(result.drawingInstructions(), GraphicsList().addGraphics(result).drawingInstructions())
    }

    @Test
    fun shouldShiftItems() {
        val result = Vector<Vector<Vector<Int>>>()
        val lines = Vector<Vector<Int>>()
        lines.add(Vector(listOf(ten.numberOfPixels, ten.numberOfPixels, ten.numberOfPixels, ten.numberOfPixels)))
        result.add(lines)
        result.add(Vector())
        val test = GraphicsList().addGraphic(LineInstructions(zero, zero, zero, zero))
        Assertions.assertNotEquals(result, test.drawingInstructions())
        Assertions.assertEquals(result, test.shift(ten, ten).drawingInstructions())
        Assertions.assertEquals(result, test.shift(ten, ten).drawingInstructions())
    }
}
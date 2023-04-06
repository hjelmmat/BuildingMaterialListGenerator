package graphics

import models.Measurement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.HashMap
import kotlin.test.BeforeTest
import kotlin.test.assertNotEquals

internal class GraphicsListTest {
    var zero = Measurement(0)
    private var ten = Measurement(10)
    val result = HashMap<String, Vector<Vector<Int>>>()

    @BeforeTest
    fun setup() {
        result.clear()
    }

    @Test
    fun shouldAddLines() {
        val lines = Vector<Vector<Int>>()
        val line = Vector(listOf(0, 0, ten.numberOfPixels, ten.numberOfPixels))
        lines.add(line)
        lines.add(line)
        result["lines"] = lines
        val instruction = LineInstructions(zero, zero, ten, ten)
        assertEquals(
            result,
            GraphicsList().addGraphic(instruction).addGraphic(instruction).drawingInstructions()
        )
    }

    @Test
    fun shouldAddRectangles() {
        val result = HashMap<String, Vector<Vector<Int>>>()
        val rectangles = Vector<Vector<Int>>()
        val rectangle = Vector(listOf(0, 0, ten.numberOfPixels, ten.numberOfPixels))
        rectangles.add(rectangle)
        result["rectangles"] = rectangles
        assertEquals(
            result,
            GraphicsList().addGraphic(RectangleInstructions(zero, zero, ten, ten)).drawingInstructions()
        )
    }

    @Test
    fun shouldAddGraphicsList() {
        val result = GraphicsList().addGraphic(LineInstructions(zero, zero, zero, zero))
        assertEquals(result.drawingInstructions(), GraphicsList().addGraphics(result).drawingInstructions())
    }

    @Test
    fun shouldShiftItems() {
        val lines = Vector<Vector<Int>>()
        lines.add(Vector(listOf(ten.numberOfPixels, ten.numberOfPixels, ten.numberOfPixels, ten.numberOfPixels)))
        result["lines"] = lines
        val test = GraphicsList().addGraphic(LineInstructions(zero, zero, zero, zero))
        assertNotEquals(result, test.drawingInstructions())
        assertEquals(result, test.shift(ten, ten).drawingInstructions())
        assertEquals(result, test.shift(ten, ten).drawingInstructions())
    }
}
package models.buildable.installable

import graphics.*
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class HeaderTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun shouldReturnMaterialList() {
        val width = Measurement(41)
        val result = MaterialList()
            .addMaterial(Lumber(width, Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(width, Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Nail.TEN_D, 48)
        assertEquals(result, Header(width).materialList())
    }

    @Test
    fun shouldReturnGraphicsList() {
        val zero = Measurement(0)
        val gap = Measurement(41)
        val twoBySixHeight = Lumber.Dimension.TWO_BY_SIX.height
        val result = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, gap, dimension.width))
            .addGraphic(RectangleInstructions(zero, dimension.width, gap, twoBySixHeight))
            .addGraphic(RectangleInstructions(zero, dimension.width.add(twoBySixHeight), gap, dimension.width))
        assertEquals(result.drawingInstructions(), Header(gap).graphicsList().drawingInstructions())
    }

    @Test
    fun shouldReturnTotalWidth() {
        val gapWidth = Measurement(44)
        assertEquals(gapWidth, Header(gapWidth).totalWidth())
    }

    @Test
    fun shouldReturnTotalHeight() {
        val result = Measurement(8, Fraction.ONE_HALF)
        assertEquals(result, Header(Measurement(41)).totalHeight())
    }
}
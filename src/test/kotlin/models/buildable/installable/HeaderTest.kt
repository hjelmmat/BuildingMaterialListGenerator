package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import models.Measurement
import models.Measurement.Fraction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class HeaderTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun shouldReturnMaterialList() {
        val width = Measurement(41)
        val result = MaterialList()
            .addMaterial(Lumber(width, Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(width, Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Nail.TEN_D, 48)
        Assertions.assertEquals(result, Header(width).materialList())
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
        Assertions.assertEquals(result.drawingInstructions(), Header(gap).graphicsList().drawingInstructions())
    }

    @Test
    fun shouldReturnTotalWidth() {
        val gapWidth = Measurement(44)
        Assertions.assertEquals(gapWidth, Header(gapWidth).totalWidth())
    }

    @Test
    fun shouldReturnTotalHeight() {
        val result = Measurement(8, Fraction.ONE_HALF)
        Assertions.assertEquals(result, Header(Measurement(41)).totalHeight())
    }
}
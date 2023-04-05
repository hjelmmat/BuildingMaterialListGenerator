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

internal class DoubleStudTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR
    private var defaultLength = Measurement(92, Fraction.FIVE_EIGHTH)

    @Test
    fun shouldIncludeTwoStudsAndExtraNails() {
        val length = Measurement(12)
        val result = MaterialList()
            .addMaterial(Lumber(length, dimension), 2)
            .addMaterial(Nail.TEN_D, 10)
        Assertions.assertEquals(result, DoubleStud(length, dimension).materialList())
        val slightlyLonger = Measurement(12, Fraction.ONE_SIXTEENTH)
        Assertions.assertEquals(result.addMaterial(Nail.TEN_D, 2), DoubleStud(slightlyLonger, dimension).materialList())
    }

    @Test
    fun shouldShowTwoStudsInDrawing() {
        val zero = Measurement(0)
        val height = Measurement(10)
        val result = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, dimension.width, height))
            .addGraphic(RectangleInstructions(dimension.width, zero, dimension.width, height))
        Assertions.assertEquals(
            result.drawingInstructions(),
            DoubleStud(height, dimension).graphicsList().drawingInstructions()
        )
    }

    @Test
    fun shouldConstructFromStud() {
        val result = DoubleStud(defaultLength, dimension)
        val fromStud = DoubleStud(Stud())
        Assertions.assertEquals(result, fromStud)
        Assertions.assertEquals(result.materialList(), fromStud.materialList())
    }

    @Test
    fun shouldNotEqualStud() {
        val stud = Stud()
        Assertions.assertNotEquals(stud, DoubleStud(defaultLength, dimension))
    }

    @Test
    fun shouldReturnTotalWidth() {
        Assertions.assertEquals(Measurement(3), DoubleStud(defaultLength, dimension).totalWidth())
    }
}
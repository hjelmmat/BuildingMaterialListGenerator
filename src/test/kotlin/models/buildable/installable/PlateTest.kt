package models.buildable.installable

import graphics.*
import models.buildable.installable.Plate.Companion.numberOfNails
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class PlateTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun plateShouldCalculateMaterial() {
        val firstLength = Measurement(3)
        val result = MaterialList()
            .addMaterial(Nail.TEN_D, 4)
            .addMaterial(Lumber(firstLength, dimension), 1)
        assertEquals(result, Plate(firstLength, dimension).materialList())
        assertEquals(result, Plate(Measurement(12), dimension).materialList())
        val secondLength = Measurement(12, Fraction.ONE_SIXTEENTH)
        val secondResult = MaterialList()
            .addMaterial(Nail.TEN_D, 6)
            .addMaterial(Lumber(secondLength, dimension), 1)
        assertEquals(secondResult, Plate(secondLength, dimension).materialList())
        assertEquals(secondResult, Plate(Measurement(24), dimension).materialList())
        val thirdLength = Measurement(24, Fraction.ONE_SIXTEENTH)
        val thirdResult = MaterialList()
            .addMaterial(Nail.TEN_D, 8)
            .addMaterial(Lumber(thirdLength, dimension), 1)
        assertEquals(thirdResult, Plate(thirdLength, dimension).materialList())
        assertEquals(thirdResult, Plate(Measurement(36), dimension).materialList())
    }

    @Test
    fun plateShouldCreateGraphicsList() {
        val zero = Measurement(0)
        val ten = Measurement(10)
        val result = GraphicsList().addGraphic(RectangleInstructions(zero, zero, ten, dimension.width))
        val test = Plate(ten, dimension)
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun plateShouldReturnTotalWidth() {
        val result = Measurement(10)
        assertEquals(result, Plate(result, dimension).totalWidth())
    }

    @Test
    fun plateShouldCalculateNumberOfNails() {
        assertEquals(4, numberOfNails(Measurement(12)))
        assertEquals(6, numberOfNails(Measurement(12, Fraction.ONE_SIXTEENTH)))
        assertEquals(6, numberOfNails(Measurement(24)))
        assertEquals(8, numberOfNails(Measurement(24, Fraction.ONE_SIXTEENTH)))
    }
}
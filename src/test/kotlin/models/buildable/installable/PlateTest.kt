package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.installable.Plate.Companion.numberOfNails
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import models.Measurement
import models.Measurement.Fraction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PlateTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun plateShouldCalculateMaterial() {
        val firstLength = Measurement(3)
        val result = MaterialList()
            .addMaterial(Nail.TEN_D, 4)
            .addMaterial(Lumber(firstLength, dimension), 1)
        Assertions.assertEquals(result, Plate(firstLength, dimension).materialList())
        Assertions.assertEquals(result, Plate(Measurement(12), dimension).materialList())
        val secondLength = Measurement(12, Fraction.ONE_SIXTEENTH)
        val secondResult = MaterialList()
            .addMaterial(Nail.TEN_D, 6)
            .addMaterial(Lumber(secondLength, dimension), 1)
        Assertions.assertEquals(secondResult, Plate(secondLength, dimension).materialList())
        Assertions.assertEquals(secondResult, Plate(Measurement(24), dimension).materialList())
        val thirdLength = Measurement(24, Fraction.ONE_SIXTEENTH)
        val thirdResult = MaterialList()
            .addMaterial(Nail.TEN_D, 8)
            .addMaterial(Lumber(thirdLength, dimension), 1)
        Assertions.assertEquals(thirdResult, Plate(thirdLength, dimension).materialList())
        Assertions.assertEquals(thirdResult, Plate(Measurement(36), dimension).materialList())
    }

    @Test
    fun plateShouldCreateGraphicsList() {
        val zero = Measurement(0)
        val ten = Measurement(10)
        val result = GraphicsList().addGraphic(RectangleInstructions(zero, zero, ten, dimension.width))
        val test = Plate(ten, dimension)
        Assertions.assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun plateShouldReturnTotalWidth() {
        val result = Measurement(10)
        Assertions.assertEquals(result, Plate(result, dimension).totalWidth())
    }

    @Test
    fun plateShouldCalculateNumberOfNails() {
        Assertions.assertEquals(4, numberOfNails(Measurement(12)))
        Assertions.assertEquals(6, numberOfNails(Measurement(12, Fraction.ONE_SIXTEENTH)))
        Assertions.assertEquals(6, numberOfNails(Measurement(24)))
        Assertions.assertEquals(8, numberOfNails(Measurement(24, Fraction.ONE_SIXTEENTH)))
    }
}
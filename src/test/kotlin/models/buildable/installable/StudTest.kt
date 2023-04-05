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

internal class StudTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun defaultStudShouldHave8FtLength() {
        val result = MaterialList()
            .addMaterial(Lumber(Measurement(92, Fraction.FIVE_EIGHTH), dimension), 1)
            .addMaterial(Nail.TEN_D, 6)
        val test = Stud().materialList()
        val error = String.format("Expected %s, was %s", result.materials(), test.materials())
        Assertions.assertEquals(result, test, error)
    }

    @Test
    fun studShouldProduceMaterialList() {
        val result = MaterialList()
            .addMaterial(Nail.TEN_D, 6)
            .addMaterial(Lumber(Measurement(92, Fraction.FIVE_EIGHTH), dimension), 1)
        Assertions.assertEquals(result, Stud().materialList())
        Assertions.assertEquals(result, Stud(Measurement(92), dimension).materialList())
        val secondResult = MaterialList()
            .addMaterial(Nail.TEN_D, 6)
            .addMaterial(Lumber(Measurement(96), dimension), 1)
        Assertions.assertEquals(
            secondResult,
            Stud(Measurement(92, Fraction.ELEVEN_SIXTEENTH), dimension).materialList()
        )
    }

    @Test
    fun studShouldBeEqualWhenOfTheSameLength() {
        Assertions.assertEquals(Stud(Measurement(95), dimension), Stud(Measurement(95), dimension))
    }

    @Test
    fun studShouldCalculateHashCode() {
        val result = -1875035575
        Assertions.assertEquals(result, Stud().hashCode())
    }

    @Test
    fun studShouldNotBeEqualWhenOfDifferentInstalledLength() {
        Assertions.assertNotEquals(Stud(), Stud(Measurement(92), dimension))
    }

    @Test
    fun studShouldCreateDrawingInstructions() {
        val zero = Measurement(0)
        val height = Measurement(10)
        val results = GraphicsList().addGraphic(RectangleInstructions(zero, zero, dimension.width, height))
        Assertions.assertEquals(
            results.drawingInstructions(),
            Stud(height, dimension).graphicsList().drawingInstructions()
        )
    }

    @Test
    fun shouldReturnTotalHeight() {
        Assertions.assertEquals(Measurement(95), Stud(Measurement(95), Lumber.Dimension.TWO_BY_FOUR).totalHeight())
    }
}
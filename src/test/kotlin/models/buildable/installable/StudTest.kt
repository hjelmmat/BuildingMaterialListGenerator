package models.buildable.installable

import graphics.*
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class StudTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun defaultStudShouldHave8FtLength() {
        val result = MaterialList()
            .addMaterial(Lumber(Measurement(92, Fraction.FIVE_EIGHTH), dimension), 1)
            .addMaterial(Nail.TEN_D, 6)
        val test = Stud().materialList()
        assertEquals(result, test)
    }

    @Test
    fun whenLengthIsGreaterThanLumberMaxLengthShouldThrow() {
        val thrown = assertFailsWith<Stud.InvalidLengthException> {
            Stud(Measurement(240, Fraction.ONE_SIXTEENTH))
        }
        assertEquals("cannot be longer than ${Lumber.FactoryLength.maxLength}", thrown.message)
        assertEquals(Lumber.FactoryLength.maxLength, thrown.maxLength)
    }

    @Test
    fun studShouldProduceMaterialList() {
        val result = MaterialList()
            .addMaterial(Nail.TEN_D, 6)
            .addMaterial(Lumber(Measurement(92, Fraction.FIVE_EIGHTH), dimension), 1)
        assertEquals(result, Stud().materialList())
        assertEquals(result, Stud(Measurement(92), dimension).materialList())
        val secondResult = MaterialList()
            .addMaterial(Nail.TEN_D, 6)
            .addMaterial(Lumber(Measurement(96), dimension), 1)
        assertEquals(
            secondResult,
            Stud(Measurement(92, Fraction.ELEVEN_SIXTEENTH), dimension).materialList()
        )
    }

    @Test
    fun studShouldBeEqualWhenOfTheSameLength() {
        assertEquals(Stud(Measurement(95), dimension), Stud(Measurement(95), dimension))
    }

    @Test
    fun studShouldCalculateHashCode() {
        val result = -1875035575
        assertEquals(result, Stud().hashCode())
    }

    @Test
    fun studShouldNotBeEqualWhenOfDifferentInstalledLength() {
        assertNotEquals(Stud(), Stud(Measurement(92), dimension))
    }

    @Test
    fun studShouldCreateDrawingInstructions() {
        val zero = Measurement(0)
        val height = Measurement(10)
        val results = GraphicsList().addGraphic(RectangleInstructions(zero, zero, dimension.width, height))
        assertEquals(
            results.drawingInstructions(),
            Stud(height, dimension).graphicsList().drawingInstructions()
        )
    }

    @Test
    fun shouldReturnTotalHeight() {
        assertEquals(Measurement(95), Stud(Measurement(95), Lumber.Dimension.TWO_BY_FOUR).totalHeight())
    }
}
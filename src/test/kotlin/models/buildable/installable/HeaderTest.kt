package models.buildable.installable

import graphics.*
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class HeaderTest {
    private val dimension = Lumber.Dimension.TWO_BY_FOUR
    private val basicWidth = Measurement(44)
    private val basicHeader = Header(Stud(basicWidth, dimension), Plate(basicWidth, dimension))

    @Test
    fun shouldReturnMaterialList() {
        val studTest = Stud(basicWidth, Lumber.Dimension.TWO_BY_SIX)
        val plateTest = Plate(basicWidth, dimension)
        val result = MaterialList()
            .addMaterials(DoubleStud(studTest).materialList())
            .addMaterials(plateTest.materialList())
            .addMaterials(plateTest.materialList())
            .addMaterial(Nail.TEN_D, 12)
        assertEquals(result.materials(), Header(studTest, plateTest).materialList().materials())
    }

    @Test
    fun shouldReturnGraphicsList() {
        val zero = Measurement(0)
        val twoBySixHeight = Lumber.Dimension.TWO_BY_SIX.height
        val result = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, basicWidth, dimension.width))
            .addGraphic(RectangleInstructions(zero, dimension.width, basicWidth, twoBySixHeight))
            .addGraphic(RectangleInstructions(zero, dimension.width.add(twoBySixHeight), basicWidth, dimension.width))
        assertEquals(
            result.drawingInstructions(),
            Header(Stud(basicWidth, Lumber.Dimension.TWO_BY_SIX), Plate(basicWidth, dimension))
                .graphicsList()
                .drawingInstructions()
        )
    }

    @Test
    fun shouldReturnTotalWidth() {
        assertEquals(basicWidth, basicHeader.totalWidth())
    }

    @Test
    fun shouldReturnTotalHeight() {
        assertEquals(Measurement(6, Fraction.ONE_HALF), basicHeader.totalHeight())
    }

    @Test
    fun whenPlateAndLoadBearingStudAreNotSameLengthShouldThrow() {
        val error = "LoadBearing stud and Plate must be the same length"
        val firstThrown = assertFailsWith<IllegalArgumentException> {
            Header(Stud(Measurement(10), dimension), Plate(Measurement(12), dimension))
        }
        assertEquals(error, firstThrown.message)
        val secondThrown = assertFailsWith<IllegalArgumentException> {
            Header(Stud(Measurement(14), dimension), Plate(Measurement(12), dimension))
        }
        assertEquals(error, secondThrown.message)
    }

}
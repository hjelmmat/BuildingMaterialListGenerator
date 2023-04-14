package models.buildable

import graphics.*
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import models.buildable.installable.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.listOf
import kotlin.test.*

internal class WallTest {
    private var validMeasurement = Measurement(100)
    private var maxTwoStudWall = Wall(Measurement(17, Fraction.ONE_HALF))
    private var threeStudWall = Wall(Measurement(25))
    private var fourStudWall = Wall(Measurement(40))

    @Test
    fun whenLengthIsTooShortWallShouldThrow() {
        val minimumLengthEdgeCase = Measurement(2, Fraction.FIFTEEN_SIXTEENTH)
        val thrown = assertFailsWith<IllegalArgumentException> { Wall(minimumLengthEdgeCase) }
        val exceptionMessage = "Length cannot be less than 3\", was 2-15/16\""
        assertEquals(exceptionMessage, thrown.message)
    }

    @Test
    fun whenLengthIsTooLongWallShouldThrow() {
        val thrown = assertFailsWith<IllegalArgumentException> {
            Wall(Measurement(240, Fraction.ONE_SIXTEENTH))
        }
        assertEquals("Wall cannot be longer than 240\"", thrown.message)
    }

    @Test
    fun whenHeightIsTooShortWallShouldThrow() {
        val minimumHeightEdgeCase = Measurement(2, Fraction.FIFTEEN_SIXTEENTH)
        val thrown = assertFailsWith<IllegalArgumentException> { Wall(validMeasurement, minimumHeightEdgeCase) }
        assertEquals("Height cannot be less than 4-1/2\", was 2-15/16\"", thrown.message)
    }

    @Test
    fun whenHeightIsTooLongWallShouldThrow() {
        val thrown = assertFailsWith<IllegalArgumentException> {
            Wall(validMeasurement, Measurement(244, Fraction.NINE_SIXTEENTH))
        }
        assertEquals("Wall cannot be taller than 243\"", thrown.message)
    }

    @Test
    fun whenHeightIsDefaultShouldCreateWall() {
        val results = MaterialList()
            .addMaterial(Lumber(Measurement(24), Lumber.Dimension.TWO_BY_FOUR), 3)
            .addMaterial(Lumber(Measurement(91), Lumber.Dimension.TWO_BY_FOUR), 3)
            .addMaterial(Nail.TEN_D, 30)
        assertEquals(results.materials(), Wall(Measurement(24)).materialList().materials())
    }

    @Test
    fun wallShouldCalculateCorrectStuds() {
        val standardStud = Stud()
        val minimumLayout = Layout().addStudAt(Measurement(0), standardStud)
            .addStudAt(Measurement(1, Fraction.ONE_HALF), standardStud)
        assertEquals(minimumLayout, Wall(Measurement(3)).layout())
        val firstStud = Measurement(0)
        val secondStud = Measurement(16)
        val twoStudLayout = Layout().addStudAt(firstStud, standardStud).addStudAt(secondStud, standardStud)
        assertEquals(twoStudLayout, maxTwoStudWall.layout())
        val doubleStud = DoubleStud(standardStud)
        val minThreeStudLayout = Layout()
            .addStudAt(firstStud, standardStud)
            .addStudAt(Measurement(14, Fraction.NINE_SIXTEENTH), doubleStud)
        val minThreeStudWall = Wall(Measurement(17, Fraction.NINE_SIXTEENTH))
        assertEquals(minThreeStudLayout, minThreeStudWall.layout())
        val threeStudLayout = Layout().addStudAt(firstStud, standardStud)
            .addStudAt(secondStud, standardStud)
            .addStudAt(Measurement(23, Fraction.ONE_HALF), standardStud)
        assertEquals(threeStudLayout, threeStudWall.layout())
        val fourStudLayout = Layout()
            .addStudAt(firstStud, standardStud)
            .addStudAt(secondStud, standardStud)
            .addStudAt(Measurement(32), standardStud)
            .addStudAt(Measurement(38, Fraction.ONE_HALF), standardStud)
        assertEquals(fourStudLayout, fourStudWall.layout())
    }

    @Test
    fun wallShouldCalculateCorrectStudHeightForLayout() {
        val shortStud = Stud(Measurement(5), Lumber.Dimension.TWO_BY_FOUR)
        val shortLayout = Layout().addStudAt(Measurement(0), shortStud)
            .addStudAt(Measurement(1, Fraction.ONE_HALF), shortStud)
        assertEquals(shortLayout, Wall(Measurement(3), Measurement(9, Fraction.ONE_HALF)).layout())
    }

    @Test
    fun wallShouldCalculateDrawingInstructions() {
        val zero = Measurement(0)
        val width = Lumber.Dimension.TWO_BY_FOUR.width
        val ten = Measurement(10)
        val result = HashMap<String, Vector<Vector<Int>>>()
        val rectangles = Vector<Vector<Int>>()
        // Add the plates
        rectangles.add(
            Vector(listOf(zero.numberOfPixels, zero.numberOfPixels, ten.numberOfPixels, width.numberOfPixels))
        )
        rectangles.add(
            Vector(listOf(zero.numberOfPixels, width.numberOfPixels, ten.numberOfPixels, width.numberOfPixels))
        )
        val heightMinusWidth = ten.subtract(width)
        rectangles.add(
            Vector(
                listOf(zero.numberOfPixels, heightMinusWidth.numberOfPixels, ten.numberOfPixels, width.numberOfPixels))
        )

        // Add the studs
        val studHeight = ten.subtract(width.multiply(3))
        val doubleWidth = width.multiply(2)
        rectangles.add(
            Vector(
                listOf(zero.numberOfPixels, doubleWidth.numberOfPixels, width.numberOfPixels, studHeight.numberOfPixels))
        )
        rectangles.add(
            Vector(
                listOf(
                    ten.subtract(width).numberOfPixels,
                    doubleWidth.numberOfPixels,
                    width.numberOfPixels,
                    studHeight.numberOfPixels
                )
            )
        )
        result["rectangles"] = rectangles
        assertEquals(result, Wall(ten, ten).graphicsList().drawingInstructions())
    }

    @Test
    fun whenDoorDoesNotFitInWallLengthShouldThrowException() {
        val thrown = assertFailsWith<IllegalArgumentException> {
            maxTwoStudWall.addADoor(StandardDoor.Bedroom, Measurement(100))
        }
        assertEquals(
            "Bedroom door cannot be installed at 100\", Bedroom door is 40\" wide and wall is 17-1/2\" long",
            thrown.message
        )
    }

    @Test
    fun whenDoorIsAddedLayoutShouldUpdate() {
        val defaultHeight = Measurement(92, Fraction.FIVE_EIGHTH)
        val materialResult = MaterialList()
            .addMaterial(Nail.TEN_D, 168)
            .addMaterial(Lumber(defaultHeight, Lumber.Dimension.TWO_BY_FOUR), 4)
            .addMaterial(Lumber(Measurement(81), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_FOUR), 5)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Lumber(Measurement(6, Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR), 4)
        val wallLength = Measurement(48)
        val test = Wall(wallLength).addADoor(StandardDoor.Bedroom, Measurement(2))

        /*
        Checking twice to ensure that the materials are not added multiple times over successive calls
         */
        assertEquals(materialResult.materials(), test.materialList().materials())
        assertEquals(materialResult, test.materialList())
        val zero = Measurement(0)
        val studWidth = Stud().totalWidth()
        val downShiftBecauseOfPlates = studWidth.multiply(2)
        val graphicsResult = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, wallLength, studWidth))
            .addGraphic(RectangleInstructions(zero, studWidth, wallLength, studWidth))
            .addGraphic(RectangleInstructions(zero, downShiftBecauseOfPlates.add(defaultHeight), wallLength, studWidth))
            .addGraphic(RectangleInstructions(zero, downShiftBecauseOfPlates, studWidth, defaultHeight))
            .addGraphic(
                RectangleInstructions(
                    wallLength.subtract(studWidth),
                    downShiftBecauseOfPlates,
                    studWidth,
                    defaultHeight
                )
            )
            .addGraphics(
                Opening(StandardDoor.Bedroom.openingWidth, StandardDoor.Bedroom.openingHeight, Stud().totalHeight())
                    .addCrippleStud(Measurement(14))
                    .addCrippleStud(Measurement(30))
                    .graphicsList().shift(Measurement(2), downShiftBecauseOfPlates),
            )
        assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun whenWindowIsAddedShouldThrowForInvalidLocation() {
        val firstThrown = assertFailsWith<IllegalArgumentException> {
            maxTwoStudWall.addAWindow(Measurement(10), Measurement(0), Measurement(10), Measurement(9))
        }
        assertEquals(
            "Window cannot be installed at 10\", Window is 16\" wide and wall is 17-1/2\" long",
            firstThrown.message
        )
    }

    @Test
    fun whenWindowIsAddedLayoutShouldUpdate() {
        val dimension = Lumber.Dimension.TWO_BY_FOUR
        val materialResult = MaterialList()
            .addMaterial(Nail.TEN_D, 178)
            // Wall Plates
            .addMaterial(Lumber(Measurement(32), dimension), 3)
            // Wall Studs
            .addMaterial(Lumber(Measurement(80), dimension), 4)
            // Trimmers
            .addMaterial(Lumber(Measurement(25), dimension), 2)
            // Header Plates
            .addMaterial(Lumber(Measurement(21, Fraction.ONE_HALF), dimension), 2)
            // Header LoadBearing
            .addMaterial(Lumber(Measurement(21, Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_SIX), 2)
            // Floor Plate
            .addMaterial(Lumber(Measurement(20), dimension), 1)
            // Floor Cripple
            .addMaterial(Lumber(Measurement(8, Fraction.ONE_HALF), dimension), 3)
            // Top Cripple
            .addMaterial(Lumber(Measurement(50), dimension), 3)

        val wallLength = Measurement(32)
        val test = Wall(wallLength, Measurement(90))
            .addAWindow(Measurement(3), Measurement(10), Measurement(20), Measurement(15))
        assertEquals(materialResult.materials(), test.materialList().materials())
    }
}
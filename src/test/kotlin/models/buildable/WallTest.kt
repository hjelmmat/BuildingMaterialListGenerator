package models.buildable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.installable.Door
import models.buildable.installable.DoubleStud
import models.buildable.installable.Layout
import models.buildable.installable.Stud
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import models.Measurement
import models.Measurement.Fraction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.listOf

internal class WallTest {
    private var validMeasurement = Measurement(100)
    private var maxTwoStudWall = Wall(Measurement(17, Fraction.ONE_HALF))
    private var threeStudWall = Wall(Measurement(25))
    private var fourStudWall = Wall(Measurement(40))

    @Test
    fun wallShouldThrowWhenLengthIsInvalid() {
        val minimumLengthEdgeCase = Measurement(2, Fraction.FIFTEEN_SIXTEENTH)
        val thrown = Assertions.assertThrows(
            IllegalArgumentException::class.java,
        ) { Wall(minimumLengthEdgeCase) }
        val exceptionMessage = "length cannot be less than 3\", was 2-15/16\""
        Assertions.assertEquals(exceptionMessage, thrown.message)
        Assertions.assertDoesNotThrow<Wall> { Wall(Measurement(3)) }
    }

    @Test
    fun wallShouldThrowWhenHeightIsInvalid() {
        val minimumHeightEdgeCase = Measurement(2, Fraction.FIFTEEN_SIXTEENTH)
        val thrown = Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Wall(validMeasurement, minimumHeightEdgeCase) }
        val exceptionMessage = "height cannot be less than 4-1/2\", was 2-15/16\""
        Assertions.assertEquals(exceptionMessage, thrown.message)
        val minimumHeight = Measurement(5)
        Assertions.assertDoesNotThrow<Wall> { Wall(validMeasurement, minimumHeight) }
    }

    @Test
    fun wallShouldCreateWallWithDefaultHeight() {
        val results = Vector<Vector<String>>()
        results.add(Vector(listOf("24\" 2x4", "3")))
        results.add(Vector(listOf("92-5/8\" 2x4", "3")))
        results.add(Vector(listOf("10d nails", "30")))
        Assertions.assertEquals(results, Wall(Measurement(24)).materials())
    }

    @Test
    @Throws(IllegalArgumentException::class)
    fun wallShouldCalculateCorrectStuds() {
        val standardStud = Stud()
        val minimumLayout = Layout().addStudAt(Measurement(0), standardStud)
            .addStudAt(Measurement(1, Fraction.ONE_HALF), standardStud)
        Assertions.assertEquals(minimumLayout, Wall(Measurement(3)).layout())
        val firstStud = Measurement(0)
        val secondStud = Measurement(16)
        val twoStudLayout = Layout().addStudAt(firstStud, standardStud).addStudAt(secondStud, standardStud)
        Assertions.assertEquals(twoStudLayout, maxTwoStudWall.layout())
        val doubleStud = DoubleStud(standardStud)
        val minThreeStudLayout = Layout()
            .addStudAt(firstStud, standardStud)
            .addStudAt(Measurement(14, Fraction.NINE_SIXTEENTH), doubleStud)
        val minThreeStudWall = Wall(Measurement(17, Fraction.NINE_SIXTEENTH))
        Assertions.assertEquals(minThreeStudLayout, minThreeStudWall.layout())
        val threeStudLayout = Layout().addStudAt(firstStud, standardStud)
            .addStudAt(secondStud, standardStud)
            .addStudAt(Measurement(23, Fraction.ONE_HALF), standardStud)
        Assertions.assertEquals(threeStudLayout, threeStudWall.layout())
        val fourStudLayout = Layout()
            .addStudAt(firstStud, standardStud)
            .addStudAt(secondStud, standardStud)
            .addStudAt(Measurement(32), standardStud)
            .addStudAt(Measurement(38, Fraction.ONE_HALF), standardStud)
        Assertions.assertEquals(fourStudLayout, fourStudWall.layout())
    }

    @Test
    fun wallShouldCalculateCorrectStudHeightForLayout() {
        val shortStud = Stud(Measurement(5), Lumber.Dimension.TWO_BY_FOUR)
        val shortLayout = Layout().addStudAt(Measurement(0), shortStud)
            .addStudAt(Measurement(1, Fraction.ONE_HALF), shortStud)
        Assertions.assertEquals(shortLayout, Wall(Measurement(3), Measurement(9, Fraction.ONE_HALF)).layout())
    }

    @Test
    fun wallShouldCalculateDrawingInstructions() {
        val zero = Measurement(0)
        val width = Lumber.Dimension.TWO_BY_FOUR.width
        val ten = Measurement(10)
        val result = Vector<Vector<Vector<Int>>>()
        result.add(Vector())
        val rectangles = Vector<Vector<Int>>()
        // Add the plates
        rectangles.add(
            Vector(
                listOf(
                    zero.numberOfPixels,
                    zero.numberOfPixels,
                    ten.numberOfPixels,
                    width.numberOfPixels
                )
            )
        )
        rectangles.add(
            Vector(
                listOf(
                    zero.numberOfPixels,
                    width.numberOfPixels,
                    ten.numberOfPixels,
                    width.numberOfPixels
                )
            )
        )
        val heightMinusWidth = ten.subtract(width)
        rectangles.add(
            Vector(
                listOf(
                    zero.numberOfPixels,
                    heightMinusWidth.numberOfPixels,
                    ten.numberOfPixels,
                    width.numberOfPixels
                )
            )
        )

        // Add the studs
        val studHeight = ten.subtract(width.multiply(3))
        val doubleWidth = width.multiply(2)
        rectangles.add(
            Vector(
                listOf(
                    zero.numberOfPixels,
                    doubleWidth.numberOfPixels,
                    width.numberOfPixels,
                    studHeight.numberOfPixels
                )
            )
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
        result.add(rectangles)
        Assertions.assertEquals(result, Wall(ten, ten).drawingInstructions())
    }

    @Test
    fun shouldThrowExceptionWhenDoorDoesNotFitInWallLength() {
        val thrown = Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { maxTwoStudWall.addADoor(Door.StandardDoor.Bedroom, Measurement(100)) }
        val exceptionMessage =
            "Door of type Bedroom cannot be installed at 100\". Wall is only 17-1/2\" long, door at 100\" of width 44\" would be outside the wall"
        Assertions.assertEquals(exceptionMessage, thrown.message)
    }

    @Test
    fun shouldThrowExceptionWhenDoorIsTooTallForWall() {
        val thrown = Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { Wall(Measurement(100), Measurement(10)).addADoor(Door.StandardDoor.Bedroom, Measurement(0)) }
        Assertions.assertEquals(
            "Door of type Bedroom cannot be installed at 0\". Wall has studs 5-1/2\" tall, door was 89-1/2\" tall",
            thrown.message,
        )
    }

    @Test
    fun whenDoorIsAddedMaterialListShouldUpdate() {
        val defaultHeight = Measurement(92, Fraction.FIVE_EIGHTH)
        val materialResult = MaterialList()
            .addMaterial(Nail.TEN_D, 168)
            .addMaterial(Lumber(defaultHeight, Lumber.Dimension.TWO_BY_FOUR), 4)
            .addMaterial(Lumber(Measurement(81), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_FOUR), 5)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Lumber(Measurement(6, Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR), 4)
        val wallLength = Measurement(48)
        val test = Wall(wallLength).addADoor(Door.StandardDoor.Bedroom, Measurement(2))

        /*
        Checking twice to ensure that the materials are not added multiple times over successive calls`
         */Assertions.assertEquals(materialResult, test.materialList())
        Assertions.assertEquals(materialResult, test.materialList())
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
                Door()
                    .addCrippleStud(Stud(), Measurement(14))
                    .addCrippleStud(Stud(), Measurement(30))
                    .graphicsList().shift(Measurement(2), downShiftBecauseOfPlates),
            )
        Assertions.assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions())
    }
}
package models.buildable.installable

import graphics.*
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import java.awt.desktop.OpenFilesHandler
import kotlin.test.*

internal class OpeningTest {
    private val dimension = Lumber.Dimension.TWO_BY_FOUR
    private val baseKingHeight = Measurement(89, Fraction.ONE_HALF)
    private val baseKing = Stud(baseKingHeight, dimension)
    private val baseTrimmerHeight = Measurement(81)
    private val baseTrimmer = Stud(baseTrimmerHeight, dimension)
    private val baseGapWidth = Measurement(38)
    private val baseHeader =
        Header(Stud(Measurement(41), Lumber.Dimension.TWO_BY_SIX), Plate(Measurement(41), dimension))
    private val baseTotalWidth = Measurement(44)
    private val door = StandardDoor.Bedroom
    private val defaultTotalHeight = Measurement(100)

    @Test
    fun shouldCreateMaterialList() {
        val result = MaterialList()
            .addMaterials(baseKing.materialList())
            .addMaterials(baseKing.materialList())
            .addMaterials(baseTrimmer.materialList())
            .addMaterials(baseTrimmer.materialList())
            .addMaterials(baseHeader.materialList())
            // There need to be nails to attach each trimmer to the king
            .addMaterial(Nail.TEN_D, Plate.numberOfNails(baseTrimmerHeight) * 2)
        assertEquals(
            result.materials(),
            Opening(baseGapWidth, baseTrimmerHeight, baseKingHeight).materialList().materials()
        )
    }

    @Test
    fun shouldCreateGraphicsList() {
        val kingHeight = baseKingHeight.add(Measurement(2))
        val result = GraphicsList()
            .addGraphics(
                Layout()
                    .addStudAt(Measurement(0), Stud(kingHeight, dimension))
                    .addStudAt(Measurement(57, Fraction.ONE_HALF), Stud(kingHeight))
                    .graphicsList()
            )
            .addGraphics(
                Layout()
                    .addStudAt(baseKing.totalWidth(), DoubleStud(baseTrimmer))
                    .addStudAt(Measurement(54, Fraction.ONE_HALF), DoubleStud(baseTrimmer))
                    .graphicsList().shift(Measurement(0), Measurement(10, Fraction.ONE_HALF))
            )
            .addGraphics(
                Header(Stud(Measurement(56), Lumber.Dimension.TWO_BY_EIGHT), Plate(Measurement(56), dimension))
                    .graphicsList().shift(dimension.width, Measurement(0))
            )
        assertEquals(
            result.drawingInstructions(),
            Opening(Measurement(50), baseTrimmerHeight, kingHeight).graphicsList().drawingInstructions()
        )
    }

    @Test
    fun whenTotalHeightIsLessThanRequiredHeightShouldThrow() {
        val error = "Total height of 50\" is too short, requires 108-1/2\""
        val thrown = assertFailsWith<IllegalArgumentException> {
            Opening(Measurement(10), Measurement(100), Measurement(50))
        }
        assertEquals(error, thrown.message)
    }

    @Test
    fun whenWidthIsZeroShouldThrow() {
        val error = "Width must be greater than 0\""
        val thrown = assertFailsWith<IllegalArgumentException> {
            Opening(Measurement(0), Measurement(100), Measurement(50))
        }
        assertEquals(error, thrown.message)
    }

    @Test
    fun whenHeightIsZeroShouldThrow() {
        val error = "Height must be greater than 0\""
        val thrown = assertFailsWith<IllegalArgumentException> {
            Opening(Measurement(10), Measurement(0), Measurement(50))
        }
        assertEquals(error, thrown.message)
    }

    @Test
    fun whenGapIsTooLongShouldThrow() {
        val thrown = assertFailsWith<IllegalArgumentException> {
            Opening(Measurement(85, Fraction.ONE_SIXTEENTH), Measurement(100), Measurement(0))
        }
        assertEquals("Opening can only be 85\" wide", thrown.message)
    }

    @Test
    fun whenGapDoesNotHaveSpaceForPlateShouldThrow() {
        val thrown = assertFailsWith<IllegalArgumentException> {
            Opening(Measurement(85, Fraction.ONE_SIXTEENTH), Measurement(100), Measurement(0), Measurement(1))
        }
        assertEquals("Height to bottom of opening must be greater than 1-1/2\" or 0\"", thrown.message)
    }

    @Test
    fun shouldReturnOpeningWidth() {
        assertEquals(
            Measurement(36),
            Opening(Measurement(30), Measurement(10), Measurement(100)).totalWidth()
        )
    }

    @Test
    fun shouldReturnOpeningHeight() {
        assertEquals(
            Measurement(100),
            Opening(Measurement(30), Measurement(10), Measurement(100)).totalHeight()
        )
    }

    @Test
    fun shouldThrowWhenCrippleStudIsAddedBeyondEdgeOfOpening() {
        val error = "Stud cannot be added at 44-1/16\". Opening is only 44\" wide"
        val conflictMeasurement = Measurement(44, Fraction.ONE_SIXTEENTH)
        val thrown = assertFailsWith<InstallableLocationConflict> {
            Opening(Measurement(38), Measurement(81), defaultTotalHeight).addCrippleStud(conflictMeasurement)
        }
        assertEquals(error, thrown.message)
        assertEquals(conflictMeasurement, thrown.conflict)
    }

    @Test
    fun whenAddingCrippleStudsShouldIgnoreCorrectStuds() {
        val bottomOfGap = Measurement(15)
        val trimmer = Stud(bottomOfGap.add(Measurement(10)), dimension)
        val rightKingStudLocation = Measurement(24, Fraction.ONE_HALF)
        val header = Header(Stud(Measurement(23), Lumber.Dimension.TWO_BY_SIX), Plate(Measurement(23), dimension))
        val headerCrippleStudHeight = baseKingHeight.subtract(trimmer.totalHeight()).subtract(header.totalHeight())
        val headerCrippleStud = Stud(headerCrippleStudHeight, dimension)
        val floorCrippleStud = Stud(bottomOfGap.subtract(dimension.width), dimension)
        val plateShift = baseKingHeight.subtract(bottomOfGap)
        val result = GraphicsList()
            .addGraphics(
                Layout()
                    .addStudAt(Measurement(0), baseKing)
                    .addStudAt(rightKingStudLocation, baseKing)
                    .graphicsList()
            )
            .addGraphics(
                Layout()
                    .addStudAt(baseKing.totalWidth(), trimmer)
                    .addStudAt(rightKingStudLocation.subtract(trimmer.totalWidth()), trimmer)
                    .graphicsList().shift(Measurement(0), baseKingHeight.subtract(trimmer.totalHeight()))
            )
            .addGraphics(header.graphicsList().shift(baseKing.totalWidth(), headerCrippleStudHeight))
            .addGraphics(Plate(Measurement(20), dimension).graphicsList().shift(Measurement(3), plateShift))
            .addGraphics(
                CrippleLayout(
                    Measurement(1, Measurement.Fraction.ONE_HALF),
                    header.totalWidth(),
                    headerCrippleStud
                )
                    .addStudAt(Measurement(3), headerCrippleStud)
                    .addStudAt(Measurement(10), headerCrippleStud)
                    .addStudAt(Measurement(21, Measurement.Fraction.ONE_HALF), headerCrippleStud)
                    .graphicsList(),
            )
            .addGraphics(
                CrippleLayout(Measurement(3), Measurement(21, Measurement.Fraction.ONE_HALF), floorCrippleStud)
                    .addStudAt(Measurement(10), floorCrippleStud)
                    .graphicsList().shift(Measurement(0), plateShift.add(dimension.width))
            )
        val test = Opening(Measurement(20), Measurement(10), baseKingHeight, bottomOfGap)
            // conflicts with first left King, is ignored
            .addCrippleStud(Measurement(0))
            // conflicts with left trimmer or first cripple, is ignored
            .addCrippleStud(Measurement(1, Measurement.Fraction.ONE_HALF))
            // conflicts with first floor cripple, but not first header cripple, should be added to header, not floor
            .addCrippleStud(Measurement(3))
            // is valid cripple stud, added to both
            .addCrippleStud(Measurement(10))
            // conflicts with last floor cripple, but not last header cripple, should be added to header, not floor
            .addCrippleStud(rightKingStudLocation.subtract(trimmer.totalWidth().multiply(2)))
            // conflicts with right trimmer, is ignored
            .addCrippleStud(rightKingStudLocation.subtract(trimmer.totalWidth()))
            // conflicts with right king, is ignored
            .addCrippleStud(rightKingStudLocation)
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())

        val materialsResult = MaterialList()
            .addMaterial(Lumber(Measurement(24), Lumber.Dimension.TWO_BY_FOUR), 6)
            .addMaterial(Lumber(Measurement(48), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(baseKingHeight, Lumber.Dimension.TWO_BY_FOUR), 7)
            .addMaterial(Lumber(Measurement(23), Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Nail.TEN_D, 178)
        assertEquals(materialsResult.materials(), test.materialList().materials())
        assertEquals(materialsResult.materials(), test.materialList().materials())
    }

    @Test
    fun shouldAddOpeningAbove() {
        val kingHeight = Measurement(75)
        val trimmerHeight = Measurement(25)
        val upperOpening = Opening(Measurement(5), Measurement(10), kingHeight, Measurement(48, Measurement.Fraction.ONE_HALF))
        val result = GraphicsList()
            .addGraphics(
                Layout()
                    .addStudAt(Measurement(0), Stud(kingHeight))
                    .addStudAt(Measurement(19, Measurement.Fraction.ONE_HALF), Stud(kingHeight))
                    .graphicsList()
            )
            .addGraphics(
                Layout()
                    .addStudAt(dimension.width, Stud(trimmerHeight, dimension))
                    .addStudAt(Measurement(18), Stud(trimmerHeight, dimension))
                    .graphicsList().shift(Measurement(0), Measurement(50))
            )
            .addGraphics(
                Header(Stud(Measurement(18), Lumber.Dimension.TWO_BY_SIX), Plate(Measurement(18), dimension))
                    .graphicsList().shift(dimension.width, Measurement(41, Measurement.Fraction.ONE_HALF))
            )
            .addGraphics(
                CrippleLayout(
                    dimension.width,
                    Measurement(18),
                    Stud(Measurement(41, Measurement.Fraction.ONE_HALF))
                )
                    .addOpeningAt(
                        Opening(
                            Measurement(5),
                            Measurement(10),
                            Measurement(41, Measurement.Fraction.ONE_HALF),
                            Measurement(15)
                        ),
                        Measurement(4)
                    )
                    .graphicsList()
            ).drawingInstructions()
        val test = Opening(Measurement(15), trimmerHeight, kingHeight).addUpperOpening(upperOpening, Measurement(4))
            .graphicsList().drawingInstructions()
        assertEquals(result, test)
    }

    @Test
    fun whenNewOpeningIsTooTallShouldThrow() {
        val height = Measurement(125)
        val test = Opening(door.openingWidth, door.openingHeight, height)
        val secondOpening = Opening(
            Measurement(10),
            Measurement(15),
            Measurement(130),
            Measurement(5)
        )
        val error = assertFailsWith<IllegalArgumentException> {
            test.addUpperOpening(
                secondOpening, Measurement(4)
            )
        }
        assertEquals(
            "Added Opening is not the same total height of the current opening, should be 125\"",
            error.message
        )

        val secondTest = Opening(door.openingWidth, Measurement(121, Fraction.ONE_HALF), Measurement(130))
        val secondError = assertFailsWith<IllegalArgumentException> {
            secondTest.addUpperOpening(
                secondOpening, Measurement(4)
            )
        }
        assertEquals("There is no space above for any opening", secondError.message)
    }

    @Test
    fun whenNewOpeningIsNotHighEngoughShouldThrow() {
        val height = Measurement(125)
        val test = Opening(door.openingWidth, door.openingHeight, height)
        val secondOpening = Opening(Measurement(10), Measurement(15), height)
        val error = assertFailsWith<IllegalArgumentException> {
            test.addUpperOpening(
                secondOpening, Measurement(4)
            )
        }
        assertEquals(
            "Cannot add opening, does not fit above current opening. Should be at least 89-1/2\"",
            error.message
        )
    }
}
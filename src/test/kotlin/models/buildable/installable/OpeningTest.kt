package models.buildable.installable

import graphics.*
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class OpeningTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR
    private var baseKingHeight = Measurement(89, Fraction.ONE_HALF)
    private var baseTrimmerHeight = Measurement(81)
    private var gapWidth = Measurement(41)
    private var baseTotalWidth = Measurement(44)
    private val door = StandardDoor.Bedroom
    private val defaultTotalHeight = Measurement(100)

    @Test
    fun shouldEqualsWhenCrippleStudsAndTypeAreTheSame() {
        val test = Opening(door.openingWidth, door.openingHeight, defaultTotalHeight)
        assertEquals(Opening(door.openingWidth, door.openingHeight, defaultTotalHeight), test)
        assertEquals(
            Opening(door.openingWidth, door.openingHeight, defaultTotalHeight).addCrippleStud(Measurement(4)),
            test.addCrippleStud(Measurement(4)),
        )
        assertNotEquals(
            Opening(door.openingWidth, door.openingHeight, Measurement(110))
                .addCrippleStud(Measurement(4)),
            test,
        )
    }

    @Test
    fun shouldCreateHashNumber() {
        val result = 704277349 // TODO: Fix the hashcode so it is stable
        assertEquals(result, Opening(door.openingWidth, door.openingHeight, defaultTotalHeight).hashCode())
    }

    @Test
    fun shouldCreateMaterialList() {
        val result = MaterialList()
            .addMaterial(Lumber(baseKingHeight, dimension), 2)
            .addMaterial(Lumber(baseTrimmerHeight, dimension), 2)
            .addMaterial(Lumber(gapWidth, dimension), 2)
            .addMaterial(Lumber(gapWidth, Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Nail.TEN_D, 104)
        assertEquals(
            result.materials(),
            Opening(Measurement(38), Measurement(81), baseKingHeight).materialList().materials()
        )
    }

    @Test
    fun shouldAddCrippleStuds() {
        val zero = Measurement(0)
        val doorHeight = Measurement(100)
        val kingStud = Stud(doorHeight, dimension)
        val topOfTrimmer = kingStud.totalHeight().subtract(baseTrimmerHeight)
        val lastTrimmerPlacement = baseTotalWidth.subtract(dimension.width.multiply(2))
        val header = Header(gapWidth)
        val topOfHeader = topOfTrimmer.subtract(header.totalHeight())
        val crippleStudGraphic = Stud(topOfHeader, dimension)
        val result = GraphicsList()
            .addGraphics(kingStud.graphicsList())
            .addGraphic(RectangleInstructions(dimension.width, topOfTrimmer, dimension.width, baseTrimmerHeight))
            .addGraphic(RectangleInstructions(lastTrimmerPlacement, topOfTrimmer, dimension.width, baseTrimmerHeight))
            .addGraphics(kingStud.graphicsList().shift(baseTotalWidth.subtract(dimension.width), zero))
            .addGraphics(header.graphicsList().shift(dimension.width, topOfHeader))
            .addGraphics(
                CrippleLayout(Measurement(41), crippleStudGraphic)
                    .addStudAt(Measurement(8, Fraction.ONE_HALF), crippleStudGraphic)
                    .graphicsList().shift(dimension.width, Measurement(0))
            )
        val crippleStuds = Stud(doorHeight, dimension)
        val test = Opening(Measurement(38), Measurement(81), defaultTotalHeight)
            // Valid Cripple Stud
            .addCrippleStud(Measurement(10))
            // Ignored Cripple Stud since it is where the left king stud is
            .addCrippleStud(zero)
            // Ignored Cripple Stud since it is where the right king stud is
            .addCrippleStud(baseTotalWidth.subtract(dimension.width).add(Measurement(1)))
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())
        val materialResult = MaterialList()
            .addMaterial(Lumber(doorHeight, dimension), 2)
            .addMaterial(Lumber(baseTrimmerHeight, dimension), 2)
            .addMaterial(Lumber(gapWidth, dimension), 2)
            .addMaterial(Lumber(gapWidth, Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Lumber(doorHeight.subtract(baseTrimmerHeight).subtract(header.totalHeight()), dimension), 3)
            .addMaterial(Nail.TEN_D, 130)
        assertEquals(materialResult.materials(), test.materialList().materials())
    }

    @Test
    fun shouldCreateGraphicsList() {
        val zero = Measurement(0)
        val topOfTrimmer = baseKingHeight.subtract(baseTrimmerHeight)
        val result = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, dimension.width, baseKingHeight))
            .addGraphic(RectangleInstructions(dimension.width, topOfTrimmer, dimension.width, baseTrimmerHeight))
            .addGraphic(
                RectangleInstructions(
                    baseTotalWidth.subtract(dimension.width.multiply(2)),
                    topOfTrimmer,
                    dimension.width,
                    baseTrimmerHeight
                )
            )
            .addGraphic(
                RectangleInstructions(
                    baseTotalWidth.subtract(dimension.width),
                    zero,
                    dimension.width,
                    baseKingHeight
                )
            )
            .addGraphics(Header(gapWidth).graphicsList().shift(dimension.width, zero))
        assertEquals(
            result.drawingInstructions(),
            Opening(Measurement(38), Measurement(81), baseKingHeight).graphicsList().drawingInstructions()
        )
    }

    @Test
    fun shouldThrowWhenCrippleStudIsAddedBeyondEdgeOfDoor() {
        val error = "Stud cannot be added at 44-1/16\". Opening is only 44\" wide"
        val conflictMeasurement = Measurement(44, Fraction.ONE_SIXTEENTH)
        val thrown = assertFailsWith<InstallableLocationConflict> {
            Opening(Measurement(38), Measurement(81), defaultTotalHeight).addCrippleStud(conflictMeasurement)
        }
        assertEquals(error, thrown.message)
        assertEquals(conflictMeasurement, thrown.conflict)
    }

    @Test
    fun whenTotalHeightIsLessThanRequiredHeightShouldThrow() {
        val error = "Total height of 50\" is too short, requires 108-1/2\""
        val thrown = assertFailsWith<IllegalArgumentException> {
            Opening(Measurement(10), Measurement(100), Measurement(50))
        }
        assertEquals(error, thrown.message)
    }
}
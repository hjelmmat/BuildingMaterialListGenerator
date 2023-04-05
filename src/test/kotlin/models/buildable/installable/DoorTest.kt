package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import models.Measurement
import models.Measurement.Fraction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DoorTest {
    private var dimension = Lumber.Dimension.TWO_BY_FOUR
    private var baseKingHeight = Measurement(89, Fraction.ONE_HALF)
    private var baseTrimmerHeight = Measurement(81)
    private var gapWidth = Measurement(41)
    private var baseTotalWidth = Measurement(44)

    @Test
    fun shouldEqualsWhenCrippleStudsAndTypeAreTheSame() {
        val test = Door(Door.StandardDoor.Bedroom)
        Assertions.assertEquals(Door(Door.StandardDoor.Bedroom), test)
        Assertions.assertEquals(
            Door(Door.StandardDoor.Bedroom).addCrippleStud(Stud(), Measurement(4)),
            test.addCrippleStud(Stud(), Measurement(4)),
        )
        Assertions.assertNotEquals(
            Door(Door.StandardDoor.Bedroom)
                .addCrippleStud(Stud(Measurement(100), dimension), Measurement(4)),
            test,
        )
    }

    @Test
    fun shouldCreateHashNumber() {
        val result = 704277349 // TODO: Fix the hashcode so it is stable
        Assertions.assertEquals(result, Door().hashCode())
    }

    @Test
    fun shouldCreateMaterialList() {
        val result = MaterialList()
            .addMaterial(Lumber(baseKingHeight, dimension), 2)
            .addMaterial(Lumber(baseTrimmerHeight, dimension), 2)
            .addMaterial(Lumber(gapWidth, dimension), 2)
            .addMaterial(Lumber(gapWidth, Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Nail.TEN_D, 104)
        Assertions.assertEquals(result, Door().materialList())
    }

    @Test
    fun shouldAddCrippleStuds() {
        val zero = Measurement(0)
        val topOfDoor = Measurement(100)
        val kingStud = Stud(topOfDoor, dimension)
        val topOfTrimmer = kingStud.totalHeight().subtract(baseTrimmerHeight)
        val lastTrimmerPlacement = baseTotalWidth.subtract(dimension.width.multiply(2))
        val header = Header(gapWidth)
        val topOfHeader = topOfTrimmer.subtract(header.totalHeight())
        val crippleDistance = Measurement(10)
        val result = GraphicsList()
            .addGraphics(kingStud.graphicsList())
            .addGraphic(RectangleInstructions(dimension.width, topOfTrimmer, dimension.width, baseTrimmerHeight))
            .addGraphic(RectangleInstructions(lastTrimmerPlacement, topOfTrimmer, dimension.width, baseTrimmerHeight))
            .addGraphics(kingStud.graphicsList().shift(baseTotalWidth.subtract(dimension.width), zero))
            .addGraphics(header.graphicsList().shift(dimension.width, topOfHeader))
            .addGraphic(RectangleInstructions(dimension.width, zero, dimension.width, topOfHeader))
            .addGraphic(RectangleInstructions(dimension.width.multiply(2), zero, dimension.width, topOfHeader))
            .addGraphic(RectangleInstructions(crippleDistance, zero, dimension.width, topOfHeader))
            .addGraphic(
                RectangleInstructions(
                    lastTrimmerPlacement.subtract(dimension.width),
                    zero,
                    dimension.width,
                    topOfHeader
                )
            )
            .addGraphic(RectangleInstructions(lastTrimmerPlacement, zero, dimension.width, topOfHeader))
        val crippleStuds = Stud(topOfDoor, Lumber.Dimension.TWO_BY_FOUR)
        val test = Door()
            // Valid Cripple Stud
            .addCrippleStud(crippleStuds, crippleDistance)
            // Ignored Cripple Stud since it is where the left king stud is
            .addCrippleStud(crippleStuds, zero)
            // Ignored Cripple Stud since it is exactly where the first cripple stud is
            .addCrippleStud(crippleStuds, dimension.width)
            // Ignored Cripple Stud since it is where the right king stud is
            .addCrippleStud(crippleStuds, baseTotalWidth.subtract(dimension.width).add(Measurement(1)))
            // Ignored Cripple Stud since it is exactly where the last cripple stud is
            .addCrippleStud(crippleStuds, baseTotalWidth.subtract(dimension.width.multiply(2)))
            // Valid Cripple Stud that needs to be shifted Right
            .addCrippleStud(crippleStuds, dimension.width.add(Measurement(1)))
            // Valid Cripple Stud that needs to be shifted Left
            .addCrippleStud(crippleStuds, lastTrimmerPlacement.subtract(Measurement(1)))
        Assertions.assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())
        val materialResult = MaterialList()
            .addMaterial(Lumber(topOfDoor, dimension), 2)
            .addMaterial(Lumber(baseTrimmerHeight, dimension), 2)
            .addMaterial(Lumber(gapWidth, dimension), 2)
            .addMaterial(Lumber(gapWidth, Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Lumber(crippleDistance, dimension), 5)
            .addMaterial(Nail.TEN_D, 150)
        Assertions.assertEquals(materialResult, test.materialList())
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
        Assertions.assertEquals(result.drawingInstructions(), Door().graphicsList().drawingInstructions())
    }

    @Test
    fun whenCrippleStudIsAddedThatConflictsWithPreviousCrippleLayoutShouldThrow() {
        val error = "Stud cannot be added at 10\". Stud already is located at 10\""
        val thrown = Assertions.assertThrows(InstallableLocationConflict::class.java
        ) {
            Door()
                .addCrippleStud(Stud(), Measurement(10))
                .addCrippleStud(Stud(), Measurement(10))
        }
        Assertions.assertEquals(error, thrown.message)
        Assertions.assertEquals(Measurement(10), thrown.conflict)
    }

    @Test
    fun shouldThrowWhenCrippleStudIsAddedBeyondEdgeOfDoor() {
        val error = "Stud cannot be added at 44-1/16\". Door is only 44\" long"
        val conflictMeasurement = Measurement(44, Fraction.ONE_SIXTEENTH)
        val thrown = Assertions.assertThrows(InstallableLocationConflict::class.java
        ) { Door().addCrippleStud(Stud(), conflictMeasurement) }
        Assertions.assertEquals(error, thrown.message)
        Assertions.assertEquals(conflictMeasurement, thrown.conflict)
    }
}
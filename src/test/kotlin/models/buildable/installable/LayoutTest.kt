package models.buildable.installable

import androidx.compose.ui.text.resolveDefaults
import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class LayoutTest {
    private val defaultStud = Stud()
    private var firstPosition = Measurement(0)
    private var secondPosition = Measurement(2)
    private var basicLayout = Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud)
    private val door = StandardDoor.Bedroom

    @Test
    fun layoutShouldAddStudWithPositionCopy() {
        val position = Measurement(0)
        assertEquals(
            basicLayout,
            Layout().addStudAt(position, defaultStud).addStudAt(position.add(secondPosition), defaultStud)
        )
    }

    @Test
    fun layoutShouldThrowWhenConflictingStudIsAdded() {
        val error = "Cannot be added at 2\", collides with Stud at 1\""
        val thrown = assertFailsWith<InstallableLocationConflict> {
            Layout().addStudAt(Measurement(1), defaultStud).addStudAt(Measurement(2), defaultStud)
        }
        assertEquals(error, thrown.message)
        assertEquals(Measurement(1), thrown.conflict)
        val secondError = "Cannot be added at 2\", collides with Stud at 2\""
        val secondThrown = assertFailsWith<InstallableLocationConflict> {
            Layout().addStudAt(Measurement(2), defaultStud).addStudAt(Measurement(2), defaultStud)
        }
        assertEquals(secondError, secondThrown.message)
        assertEquals(Measurement(2), secondThrown.conflict)
        val thirdError = "Cannot be added at 4-1/2\", collides with Stud at 5\""
        val thirdThrown = assertFailsWith<InstallableLocationConflict> {
            Layout().addStudAt(Measurement(5), defaultStud).addStudAt(Measurement(4, Fraction.ONE_HALF), defaultStud)
        }
        assertEquals(thirdError, thirdThrown.message)
        assertEquals(Measurement(5), thirdThrown.conflict)
    }

    @Test
    fun layoutShouldReturnMaterials() {
        val result = MaterialList()
            .addMaterial(Nail.TEN_D, 12)
            .addMaterial(Lumber(Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2)
        assertEquals(result, basicLayout.materialList())
    }

    @Test
    fun layoutShouldDetermineEquals() {
        val resultLayout = Layout()
            .addStudAt(firstPosition, defaultStud)
            .addStudAt(secondPosition, defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(10))
        val testingLayout = Layout()
            .addStudAt(firstPosition, defaultStud)
            .addStudAt(secondPosition, defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(10))
        assertEquals(resultLayout, testingLayout)
    }

    @Test
    fun layoutShouldCreateGraphicsList() {
        val width = Lumber.Dimension.TWO_BY_FOUR.width
        val zero = Measurement(0)
        val distance = Measurement(10)
        val result = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, width, distance))
            .addGraphic(RectangleInstructions(distance, zero, width, distance))
        val stud = Stud(distance, Lumber.Dimension.TWO_BY_FOUR)
        assertEquals(
            result.drawingInstructions(),
            Layout()
                .addStudAt(Measurement(0), stud)
                .addStudAt(distance, stud)
                .graphicsList()
                .drawingInstructions(),
        )
    }

    @Test
    fun whenAOpeningIsAddedLayoutShouldReplaceStudsInRangeWithOpening() {
        val zero = Measurement(0)
        val resultDoor = Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()).addCrippleStud(zero)
        val result = Layout().addStudAt(Measurement(100), defaultStud).addOpeningAt(resultDoor, zero)
        assertEquals(
            result,
            Layout()
                .addStudAt(Measurement(100), defaultStud)
                .addStudAt(zero, defaultStud)
                .addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), zero),
        )
    }

    @Test
    fun whenStudsAreDifferentHeightsGraphicListShouldDrawCorrectly() {
        val width = Lumber.Dimension.TWO_BY_FOUR.width
        val zero = Measurement(0)
        val distance = Measurement(10)
        val shorterDistance = Measurement(8)
        val result = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, width, distance))
            .addGraphic(RectangleInstructions(distance, Measurement(2), width, shorterDistance))
        val stud = Stud(distance, Lumber.Dimension.TWO_BY_FOUR)
        assertEquals(
            result.drawingInstructions(),
            Layout()
                .addStudAt(Measurement(0), stud)
                .addStudAt(distance, Stud(shorterDistance, Lumber.Dimension.TWO_BY_FOUR))
                .graphicsList()
                .drawingInstructions(),
        )
    }

    @Test
    fun layoutShouldReturnTotalWidth() {
        val result = Measurement(10)
        val test = Layout().addStudAt(Measurement(8, Fraction.ONE_HALF), defaultStud)
        assertEquals(result, test.totalWidth())
        val secondResult = Measurement(50)
        test.addStudAt(Measurement(48, Fraction.ONE_HALF), defaultStud)
            .addOpeningAt(
                Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()),
                Measurement(4, Fraction.ONE_HALF)
            )
        assertEquals(secondResult, test.totalWidth())
        val thirdResult = Measurement(55)
        test.addStudAt(Measurement(53, Fraction.ONE_HALF), defaultStud)
        assertEquals(thirdResult, test.totalWidth())
    }

    @Test
    fun shouldReturnIsEmpty() {
        assertTrue(Layout().isEmpty)
        assertFalse(Layout().addStudAt(Measurement(0), defaultStud).isEmpty)
    }

    @Test
    fun layoutShouldAddOpening() {
        val zero = Measurement(0)
        val materialResult = MaterialList()
            .addMaterial(Nail.TEN_D, 148)
            .addMaterial(Lumber(Measurement(92, Fraction.FIVE_EIGHTH), Lumber.Dimension.TWO_BY_FOUR), 4)
            .addMaterial(Lumber(Measurement(81), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Lumber(Measurement(6, Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR), 4)
        val test = Layout().addStudAt(zero, defaultStud)
            .addStudAt(Measurement(16), defaultStud)
            .addStudAt(Measurement(32), defaultStud)
            .addStudAt(Measurement(50), defaultStud)
            .addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(2))
        assertEquals(materialResult.materials(), test.materialList().materials())
        val graphicsResult = GraphicsList()
            .addGraphic(RectangleInstructions(zero, zero, defaultStud.totalWidth(), defaultStud.totalHeight()))
            .addGraphic(
                RectangleInstructions(
                    Measurement(50),
                    zero,
                    defaultStud.totalWidth(),
                    defaultStud.totalHeight()
                )
            )
        graphicsResult.addGraphics(
            Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight())
                .addCrippleStud(Measurement(14))
                .addCrippleStud(Measurement(30))
                .graphicsList().shift(Measurement(2), zero)
        )
        assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun layoutShouldAdjustOutsideStudsForAddedOpenings() {
        val result = Layout().addStudAt(Measurement(4, Fraction.ONE_HALF), defaultStud)
            .addStudAt(Measurement(46), defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addOpeningAt(
                Opening(
                    door.openingWidth,
                    door.openingHeight,
                    defaultStud.totalHeight()
                ).addCrippleStud(Measurement(24)),
                Measurement(6)
            )
        val test = Layout().addStudAt(Measurement(5), defaultStud)
            .addStudAt(Measurement(30), defaultStud)
            .addStudAt(Measurement(45), defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(6))
        assertEquals(result.graphicsList().drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun addOpeningShouldThrowIfStudCannotBeMoved() {
        val error = "Cannot be added at 1\". Stud at 0\" cannot be moved."
        val test = Layout().addStudAt(Measurement(0), defaultStud).addStudAt(Measurement(100), defaultStud)
        val thrown = assertFailsWith<InstallableLocationConflict> {
            test.addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(1))
        }
        assertEquals(error, thrown.message)
        assertEquals(Measurement(0), thrown.conflict)
        assertEquals(
            Layout().addStudAt(Measurement(0), defaultStud).addStudAt(Measurement(100), defaultStud),
            test
        )
        val secondError = "Cannot be added at 0\". Stud at 39\" cannot be moved."
        val secondThrown = assertFailsWith<InstallableLocationConflict> {
            Layout()
                .addStudAt(Measurement(39), defaultStud)
                .addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(0))
        }
        assertEquals(secondError, secondThrown.message)
        assertEquals(Measurement(39), secondThrown.conflict)
    }

    @Test
    fun addOpeningShouldThrowWhenRightSideOfOpeningIsOutsideLayout() {
        val error = "Cannot be added at 0\", is only 11-1/2\" wide and opening ends at 40\""
        val test = Layout().addStudAt(Measurement(10), defaultStud)
        val thrown = assertFailsWith<InstallableLocationConflict> {
            test.addOpeningAt(Opening(door.openingWidth, door.openingHeight, defaultStud.totalHeight()), Measurement(0))
        }
        assertEquals(error, thrown.message)
        assertEquals(Measurement(0), thrown.conflict)
    }

    @Test
    fun shouldReturnTotalHeight() {
        assertEquals(
            Measurement(100),
            Layout()
                .addStudAt(Measurement(50), Stud(Measurement(100), Lumber.Dimension.TWO_BY_FOUR))
                .totalHeight(),
        )
    }

    @Test
    fun whenAddingOpeningShouldReplaceEdgeStuds() {
        val testOpening = Opening(Measurement(5, Measurement.Fraction.ONE_HALF), Measurement(20), Measurement(50))
        val result = GraphicsList()
            .addGraphics(testOpening.graphicsList())
        val test = Layout()
            .addStudAt(Measurement(0), Stud(Measurement(50)))
            .addStudAt(Measurement(10), Stud(Measurement(50)))
            .addOpeningAt(testOpening, Measurement(0))
        assertEquals(result, test.graphicsList())
    }

    @Test
    fun whenAddingOpeningShouldAddToOpening() {
        val height = Measurement(125)
        val openingResult = Opening(door.openingWidth, door.openingHeight, height)
            .addCrippleStud(Measurement(7))
        val secondOpening = Opening(Measurement(10), Measurement(15), height, Measurement(94, Fraction.ONE_HALF))
        openingResult.addUpperOpening(secondOpening, Measurement(4))
        val result = Layout().addStudAt(Measurement(100), Stud(height)).addOpeningAt(openingResult, Measurement(3))
        val test = Layout().addStudAt(Measurement(100), Stud(height))
            .addStudAt(Measurement(10), Stud(height))
            .addOpeningAt(Opening(door.openingWidth, door.openingHeight, height), Measurement(3))
            .addOpeningAt(
                Opening(Measurement(10), Measurement(15), height, Measurement(94, Fraction.ONE_HALF)),
                Measurement(7)
            )
        assertEquals(result, test)
    }
}

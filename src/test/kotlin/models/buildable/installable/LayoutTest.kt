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

internal class LayoutTest {
    private var defaultStud = Stud()
    private var firstPosition = Measurement(0)
    private var secondPosition = Measurement(1, Fraction.ONE_HALF)
    private var basicLayout = Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud)

    @Test
    fun layoutShouldAddStudWithPositionCopy() {
        val position = Measurement(0)
        Assertions.assertEquals(
            basicLayout,
            Layout().addStudAt(position, defaultStud).addStudAt(position.add(secondPosition), defaultStud)
        )
    }

    @Test
    fun layoutShouldThrowWhenConflictingStudIsAdded() {
        val error = "Stud cannot be added at 2\". Stud already is located at 1\""
        val thrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) {
            Layout().addStudAt(Measurement(1), defaultStud).addStudAt(Measurement(2), defaultStud)
        }
        Assertions.assertEquals(error, thrown.message)
        Assertions.assertEquals(Measurement(1), thrown.conflict)
        val secondError = "Stud cannot be added at 2\". Stud already is located at 2\""
        val secondThrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) {
            Layout().addStudAt(Measurement(2), defaultStud).addStudAt(Measurement(2), defaultStud)
        }
        Assertions.assertEquals(secondError, secondThrown.message)
        Assertions.assertEquals(Measurement(2), secondThrown.conflict)
        val thirdError = "Stud cannot be added at 4-1/2\". Stud already is located at 5\""
        val thirdThrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) {
            Layout().addStudAt(Measurement(5), defaultStud).addStudAt(Measurement(4, Fraction.ONE_HALF), defaultStud)
        }
        Assertions.assertEquals(thirdError, thirdThrown.message)
        Assertions.assertEquals(Measurement(5), thirdThrown.conflict)
    }

    @Test
    fun layoutShouldReturnMaterials() {
        val result = MaterialList()
            .addMaterial(Nail.TEN_D, 12)
            .addMaterial(Lumber(Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2)
        Assertions.assertEquals(result, basicLayout.materialList())
    }

    @Test
    fun layoutShouldDetermineEquals() {
        val resultLayout = Layout()
            .addStudAt(firstPosition, defaultStud)
            .addStudAt(secondPosition, defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addDoorAt(Door(), Measurement(10))
        val testingLayout = Layout()
            .addStudAt(firstPosition, defaultStud)
            .addStudAt(secondPosition, defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addDoorAt(Door(), Measurement(10))
        Assertions.assertEquals(resultLayout, testingLayout)
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
        Assertions.assertEquals(
            result.drawingInstructions(),
            Layout()
                .addStudAt(Measurement(0), stud)
                .addStudAt(distance, stud)
                .graphicsList()
                .drawingInstructions(),
        )
    }

    @Test
    fun whenADoorIsAddedLayoutShouldReplaceStudsInRangeWithDoor() {
        val zero = Measurement(0)
        val resultDoor = Door().addCrippleStud(defaultStud, zero)
        val result = Layout().addStudAt(Measurement(100), defaultStud).addDoorAt(resultDoor, zero)
        Assertions.assertEquals(
            result,
            Layout()
                .addStudAt(Measurement(100), defaultStud)
                .addStudAt(zero, defaultStud)
                .addDoorAt(Door(), zero),
        )
    }

    @Test
    fun whenADoorIsAddedThatOverlapsAnotherDoorAddDoorAtShouldThrow() {
        val error = "Door cannot be added at 44\". Door already is located at 1\""
        val thrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) {
            Layout()
                .addStudAt(Measurement(100), defaultStud)
                .addDoorAt(Door(), Measurement(1))
                .addDoorAt(Door(), Measurement(44))
        }
        Assertions.assertEquals(error, thrown.message)
        Assertions.assertEquals(Measurement(1), thrown.conflict)
        val secondError = "Door cannot be added at 44\". Door already is located at 45\""
        val secondThrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) {
            Layout()
                .addStudAt(Measurement(100), defaultStud)
                .addDoorAt(Door(), Measurement(45))
                .addDoorAt(Door(), Measurement(44))
        }
        Assertions.assertEquals(secondError, secondThrown.message)
        Assertions.assertEquals(Measurement(45), secondThrown.conflict)
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
        Assertions.assertEquals(
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
        Assertions.assertEquals(result, test.totalWidth())
        val secondResult = Measurement(50)
        test.addStudAt(Measurement(48, Fraction.ONE_HALF), defaultStud)
            .addDoorAt(Door(), Measurement(4, Fraction.ONE_HALF))
        Assertions.assertEquals(secondResult, test.totalWidth())
        val thirdResult = Measurement(55)
        test.addStudAt(Measurement(53, Fraction.ONE_HALF), defaultStud)
        Assertions.assertEquals(thirdResult, test.totalWidth())
    }

    @Test
    fun shouldReturnIsEmpty() {
        Assertions.assertTrue(Layout().isEmpty)
        Assertions.assertFalse(Layout().addStudAt(Measurement(0), defaultStud).isEmpty)
    }

    @Test
    fun layoutShouldAddDoor() {
        val zero = Measurement(0)
        val test = Layout().addStudAt(zero, defaultStud)
            .addStudAt(Measurement(16), defaultStud)
            .addStudAt(Measurement(32), defaultStud)
            .addStudAt(Measurement(50), defaultStud)
            .addDoorAt(Door(), Measurement(2))
        val materialResult = MaterialList()
            .addMaterial(Nail.TEN_D, 148)
            .addMaterial(Lumber(Measurement(92, Fraction.FIVE_EIGHTH), Lumber.Dimension.TWO_BY_FOUR), 4)
            .addMaterial(Lumber(Measurement(81), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_FOUR), 2)
            .addMaterial(Lumber(Measurement(41), Lumber.Dimension.TWO_BY_SIX), 2)
            .addMaterial(Lumber(Measurement(6, Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR), 4)
        Assertions.assertEquals(materialResult.materials(), test.materialList().materials())
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
            Door()
                .addCrippleStud(defaultStud, Measurement(14))
                .addCrippleStud(defaultStud, Measurement(30))
                .graphicsList().shift(Measurement(2), zero))
        Assertions.assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun layoutShouldAdjustOutsideStudsForAddedDoors() {
        val result = Layout().addStudAt(Measurement(4, Fraction.ONE_HALF), defaultStud)
            .addStudAt(Measurement(50), defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addDoorAt(Door().addCrippleStud(defaultStud, Measurement(24)), Measurement(6))
        val test = Layout().addStudAt(Measurement(5), defaultStud)
            .addStudAt(Measurement(30), defaultStud)
            .addStudAt(Measurement(49), defaultStud)
            .addStudAt(Measurement(100), defaultStud)
            .addDoorAt(Door(), Measurement(6))
        Assertions.assertEquals(result.graphicsList().drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun addDoorShouldThrowIfStudCannotBeMoved() {
        val error = "Door cannot be added at 1\". Stud at 0\" cannot be moved."
        val test = Layout().addStudAt(Measurement(0), defaultStud).addStudAt(Measurement(100), defaultStud)
        val thrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) { test.addDoorAt(Door(), Measurement(1)) }
        Assertions.assertEquals(error, thrown.message)
        Assertions.assertEquals(Measurement(0), thrown.conflict)
        Assertions.assertEquals(
            Layout().addStudAt(Measurement(0), defaultStud).addStudAt(Measurement(100), defaultStud),
            test
        )
        val secondError = "Door cannot be added at 0\". Stud at 43\" cannot be moved."
        val secondThrown = Assertions.assertThrows(
            InstallableLocationConflict::class.java
        ) {
            Layout()
                .addStudAt(Measurement(43), defaultStud)
                .addDoorAt(Door(), Measurement(0))
        }
        Assertions.assertEquals(secondError, secondThrown.message)
        Assertions.assertEquals(Measurement(43), secondThrown.conflict)
    }

    @Test
    fun addDoorShouldThrowWhenRightSideOfDoorIsOutsideLayout() {
        val error = "Door cannot be added at 0\". Layout is only 11-1/2\" long and door ends at 44\""
        val test = Layout().addStudAt(Measurement(10), defaultStud)
        val thrown = Assertions.assertThrows(InstallableLocationConflict::class.java
        ) { test.addDoorAt(Door(), Measurement(0)) }
        Assertions.assertEquals(error, thrown.message)
        Assertions.assertEquals(Measurement(0), thrown.conflict)
        test.addStudAt(Measurement(44), defaultStud)
        Assertions.assertDoesNotThrow<Layout> { test.addDoorAt(Door(), Measurement(0)) }
    }

    @Test
    fun shouldReturnTotalHeight() {
        Assertions.assertEquals(
            Measurement(100),
            Layout()
                .addStudAt(Measurement(50), Stud(Measurement(100), Lumber.Dimension.TWO_BY_FOUR))
                .totalHeight(),
        )
    }
}
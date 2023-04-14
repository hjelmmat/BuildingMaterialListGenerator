package models.buildable.installable

import graphics.GraphicsList
import models.Measurement
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import kotlin.test.*

class CrippleLayoutTest {
    @Test
    fun shouldAddFirstAndLastStuds() {
        val graphicResult = GraphicsList()
            .addGraphics(Stud().graphicsList())
            .addGraphics(Stud().graphicsList().shift(Measurement(10), Measurement(0)))
        val test = CrippleLayout(Measurement(0), Measurement(10), Stud())
        assertEquals(graphicResult, test.graphicsList())

        val materialResult = MaterialList()
            .addMaterials(Stud().materialList())
            .addMaterials(Stud().materialList())
            .addMaterial(Nail.TEN_D, Plate.numberOfNails(Stud().totalHeight()) * 2)
        assertEquals(materialResult, test.materialList())
    }

    @Test
    fun shouldIncludeExtraNailsForFirstAndLastStuds() {
        val result = Layout()
            .addStudAt(Measurement(0), Stud())
            .addStudAt(Measurement(10), Stud())
            .materialList()
            .addMaterial(Nail.TEN_D, 36)
        val test = CrippleLayout(Measurement(0), Measurement(10), Stud()).materialList()
        assertEquals(result.materials(), test.materials())
    }

    @Test
    fun whenStudConflictsWithFirstAddedStudShouldMove() {
        val crippleStuds = Stud()
        val width = Measurement(10)
        val result = Layout()
            .addStudAt(Measurement(0), DoubleStud(crippleStuds))
            .addStudAt(Measurement(8, Measurement.Fraction.ONE_HALF), DoubleStud(crippleStuds))
        val test = CrippleLayout(Measurement(0), width, crippleStuds)
            // Ignored Cripple Stud since it is exactly where the first cripple stud is
            .addStudAt(Measurement(0), crippleStuds)
            // Ignored Cripple Stud since it is exactly where the last cripple stud is
            .addStudAt(width, crippleStuds)
            // Valid Cripple Stud that needs to be shifted Right
            .addStudAt(Measurement(1), crippleStuds,)
            // Valid Cripple Stud that needs to be shifted Left
            .addStudAt(width.subtract(Measurement(1)), crippleStuds,)
        assertEquals(result.graphicsList().drawingInstructions(), test.graphicsList().drawingInstructions())
    }

    @Test
    fun whenStudTouchesOutsideStudShouldTurnDouble() {
        val crippleStuds = Stud()
        val width = Measurement(10)
        val result = Layout()
            .addStudAt(Measurement(0), DoubleStud(crippleStuds))
            .addStudAt(Measurement(8, Measurement.Fraction.ONE_HALF), DoubleStud(crippleStuds))
            .materialList().addMaterial(Nail.TEN_D, Plate.numberOfNails(crippleStuds.totalHeight()) * 2).materials()
        val test = CrippleLayout(Measurement(0), width, crippleStuds)
            // Valid Cripple Stud that needs to be shifted Right
            .addStudAt(crippleStuds.totalWidth(), crippleStuds)
            // Valid Cripple Stud that needs to be shifted Left
            .addStudAt(width.subtract(crippleStuds.totalWidth()), crippleStuds)
        assertEquals(result, test.materialList().materials())
    }

    @Test
    fun whenCrippleStudIsAddedThatConflictsWithPreviousCrippleLayoutShouldThrow() {
        val error = "Cannot be added at 10\", collides with Stud at 10\""
        val thrown = assertFailsWith<Layout.InstallableLocationConflict>{
            CrippleLayout(Measurement(0), Measurement(20), Stud())
                .addStudAt(Measurement(10), Stud())
                .addStudAt(Measurement(10), Stud())
        }
        assertEquals(error, thrown.message)
        assertEquals(Measurement(10), thrown.conflict)
    }

    @Test
    fun whenCrippleStudIsAddedOutsideStudsShouldIgnore() {
        val result = CrippleLayout(Measurement(5), Measurement(10), Stud())
        val test = CrippleLayout(Measurement(5), Measurement(10), Stud())
            .addStudAt(Measurement(0), Stud())
            .addStudAt(Measurement(15), Stud())
        assertEquals(result, test)
    }
}
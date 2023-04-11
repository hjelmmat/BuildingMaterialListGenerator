package models.buildable.installable

import models.Measurement
import models.buildable.material.Nail
import kotlin.test.*

class CrippleLayoutTest {
    @Test
    fun shouldAddFirstAndLastStuds() {
        val result = Layout()
            .addStudAt(Measurement(0), Stud())
            .addStudAt(Measurement(8, Measurement.Fraction.ONE_HALF), Stud())
        val test = CrippleLayout(Measurement(10), Stud())
        assertEquals(result, test as Layout)
    }

    @Test
    fun shouldIncludeExtraNailsForFirstAndLastStuds() {
        val result = Layout()
            .addStudAt(Measurement(0), Stud())
            .addStudAt(Measurement(8, Measurement.Fraction.ONE_HALF), Stud())
            .materialList()
            .addMaterial(Nail.TEN_D, 36)
        val test = CrippleLayout(Measurement(10), Stud()).materialList()
        assertEquals(result.materials(), test.materials())
    }

    @Test
    fun whenStudConflictsWithFirstAddedStudShouldMove() {
        val crippleStuds = Stud()
        val width = Measurement(10)
        val test = CrippleLayout(width, crippleStuds)
            // Ignored Cripple Stud since it is exactly where the first cripple stud is
            .addStudAt(Measurement(0), crippleStuds)
            // Ignored Cripple Stud since it is exactly where the last cripple stud is
            .addStudAt(width.subtract(crippleStuds.totalWidth()), crippleStuds)
            // Valid Cripple Stud that needs to be shifted Right
            .addStudAt(Measurement(1), crippleStuds,)
            // Valid Cripple Stud that needs to be shifted Left
            .addStudAt(width.subtract(Measurement(1)), crippleStuds,)
        val result = Layout()
            .addStudAt(Measurement(0), DoubleStud(crippleStuds))
            .addStudAt(Measurement(7), DoubleStud(crippleStuds))
        assertEquals(result, test)
    }

    @Test
    fun whenCrippleStudIsAddedThatConflictsWithPreviousCrippleLayoutShouldThrow() {
        val error = "Cannot be added at 10\", collides with Stud at 10\""
        val thrown = assertFailsWith<Layout.InstallableLocationConflict>{
            CrippleLayout(Measurement(20), Stud())
                .addStudAt(Measurement(10), Stud())
                .addStudAt(Measurement(10), Stud())
        }
        assertEquals(error, thrown.message)
        assertEquals(Measurement(10), thrown.conflict)
    }
}
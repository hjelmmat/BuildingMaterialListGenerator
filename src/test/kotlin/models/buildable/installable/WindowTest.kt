package models.buildable.installable

import models.Measurement
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import kotlin.test.Test
import kotlin.test.assertEquals


class WindowTest {
    private val dimension = Lumber.Dimension.TWO_BY_FOUR

    @Test
    fun shouldCreateOpeningWithAdjustedHeight() {
        val test = Window(
            windowWidth = Measurement(10),
            windowHeight = Measurement(15),
            bottomOfWindowHeight = Measurement(20),
            totalOpeningHeight = Measurement(50)
        )
        val result = Opening(Measurement(10), Measurement(15).add(Measurement(20)), Measurement(50))
        val floorLayout = CrippleLayout(
            Measurement(10), Stud(Measurement(18, Measurement.Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR)
        )
        val graphicsResult = result
            .graphicsList()
            .addGraphics(
                Plate(Measurement(10), dimension)
                    .graphicsList().shift(Measurement(3), Measurement(30))
            )
            .addGraphics(
                floorLayout.graphicsList().shift(Measurement(3), Measurement(31, Measurement.Fraction.ONE_HALF))
            )
        assertEquals(graphicsResult.drawingInstructions(), test.graphicsList().drawingInstructions())

        val materialsResult = result
            .materialList()
            .addMaterials(floorLayout.materialList())
            .addMaterials(Stud(Measurement(10), Lumber.Dimension.TWO_BY_FOUR).materialList())
        assertEquals(materialsResult.materials(), test.materialList().materials())
    }

    @Test
    fun whenAddingCrippleStudsShouldAddToFloorLayoutToo() {
        val floorCripple = Stud(Measurement(18, Measurement.Fraction.ONE_HALF), dimension)
        val result = Opening(
            width = Measurement(10),
            height = Measurement(35),
            totalHeight = Measurement(50),
        )
            .addCrippleStud(Measurement(5))
            .graphicsList()
            .addGraphics(
                Plate(Measurement(10), dimension)
                    .graphicsList().shift(Measurement(3), Measurement(30))
            )
            .addGraphics(
                CrippleLayout(
                    Measurement(10),
                    Stud(Measurement(18, Measurement.Fraction.ONE_HALF), Lumber.Dimension.TWO_BY_FOUR)
                )
                    .addStudAt(Measurement(2), floorCripple)
                    .graphicsList().shift(Measurement(3), Measurement(31, Measurement.Fraction.ONE_HALF))
            )
        val test = Window(
            windowWidth = Measurement(10),
            windowHeight = Measurement(15),
            bottomOfWindowHeight = Measurement(20),
            totalOpeningHeight = Measurement(50)
        )
            .addCrippleStud(Measurement(5))
        assertEquals(result.drawingInstructions(), test.graphicsList().drawingInstructions())
    }
}
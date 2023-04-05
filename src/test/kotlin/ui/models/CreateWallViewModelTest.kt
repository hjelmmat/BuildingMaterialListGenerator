package ui.models

import models.Measurement
import models.buildable.Wall
import kotlin.test.Test
import kotlin.test.*


class CreateWallViewModelTest {
    @Test
    fun shouldValidateMeasurements() {
        val test = CreateWallViewModel()
        assertEquals(false, test.validMeasurements())
        test.heightMeasurement.integerValue = "10"
        assertEquals(false, test.validMeasurements())
        test.lengthMeasurement.integerValue = "10"
        assertEquals(true, test.validMeasurements())
    }

    @Test
    fun shouldCalculateMaterials() {
        val test = CreateWallViewModel()
        test.heightMeasurement.integerValue = "10"
        test.lengthMeasurement.integerValue = "15"
        test.calculateMaterials()
        val result = MaterialsViewModel().updateData(Wall(Measurement(15), Measurement(10)).materials())
        assertEquals(result.data, test.materials.data)
    }

    @Test
    fun shouldUpdateGraphic() {
        val test = CreateWallViewModel()
        test.heightMeasurement.integerValue = "10"
        test.lengthMeasurement.integerValue = "15"
        test.calculateMaterials()
        val result = GraphicsViewModel().updateData(Wall(Measurement(15), Measurement(10)).drawingInstructions())
        assertEquals(result.data, test.graphics.data)
    }
}
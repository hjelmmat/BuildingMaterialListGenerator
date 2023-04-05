package ui.models

import models.buildable.Wall


class CreateWallViewModel {
    val heightMeasurement = MeasurementViewModel("Height", Wall.minimumHeight)
    val lengthMeasurement = MeasurementViewModel("Width", Wall.minimumWidth)
    val materials = MaterialsViewModel()
    val graphics = GraphicsViewModel()

    fun validMeasurements(): Boolean {
        return heightMeasurement.validValue() && lengthMeasurement.validValue()
    }

    fun calculateMaterials() {
        if (validMeasurements()) {
            val wall = Wall(lengthMeasurement.asMeasurement(), heightMeasurement.asMeasurement())
            materials.updateData(wall.materials())
            graphics.updateData(wall.drawingInstructions())
        }
    }
}
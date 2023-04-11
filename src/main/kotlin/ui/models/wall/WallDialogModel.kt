package ui.models.wall

import models.Measurement
import models.buildable.Wall
import models.buildable.installable.StandardDoor
import ui.models.graphics.GraphicsViewModel
import ui.models.materials.MaterialsViewModel

class WallDialogModel {
    val createWallModel = CreateWallViewModel(::createWall)
    val doorModel = AddDoorViewModel(::addDoor)
    val windowModel = AddWindowViewModel(::addWindow)
    val materials = MaterialsViewModel()
    val graphics = GraphicsViewModel()
    private var wall = Wall(Measurement(10), Measurement(10))

    init {
        updateContents()
    }

    private fun createWall(length: Measurement, height: Measurement) {
        wall = Wall(length, height)
        updateContents()
    }

    private fun addDoor(position: Measurement) {
        wall.addADoor(StandardDoor.Bedroom, position)
        updateContents()
    }

    private fun addWindow(
        atLocation: Measurement,
        bottomOfWindowHeight: Measurement,
        windowWidth: Measurement,
        windowHeight: Measurement,
    ) {
        wall.addAWindow(atLocation, bottomOfWindowHeight, windowWidth, windowHeight)
        updateContents()

    }

    private fun updateContents() {
        materials.updateData(wall.materialList().materials())
        graphics.updateData(wall.graphicsList().drawingInstructions())
    }
}

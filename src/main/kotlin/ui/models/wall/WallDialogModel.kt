package ui.models.wall

import models.Measurement
import models.buildable.Wall
import models.buildable.installable.Door

class WallDialogModel {
    val createWallModel = CreateWallViewModel(::createWall)
    val doorModel = AddDoorViewModel(::addDoor)
    val wallContentsModel = WallContentsViewModel()
    private var wall = Wall(Measurement(10), Measurement(10))

    private fun createWall(length: Measurement, height: Measurement) {
        wall = Wall(length, height)
        updateContents()
    }

    private fun addDoor(position: Measurement) {
        wall.addADoor(Door.StandardDoor.Bedroom, position)
        updateContents()
    }

    private fun updateContents() {
        wallContentsModel.update(wall.materialList().materials(), wall.graphicsList().drawingInstructions())
    }
}

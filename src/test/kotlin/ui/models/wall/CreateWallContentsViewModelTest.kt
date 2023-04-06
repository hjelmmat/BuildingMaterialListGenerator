package ui.models.wall

import models.Measurement
import models.buildable.Wall
import ui.models.graphics.GraphicsViewModel
import ui.models.materials.MaterialsViewModel
import kotlin.test.Test
import kotlin.test.*


class CreateWallContentsViewModelTest {
    @Test
    fun shouldCalculateMaterials() {
        val test = WallContentsViewModel()
        val wall = Wall(Measurement(15), Measurement(10))
        test.update(wall.materialList().materials(), wall.graphicsList().drawingInstructions())
        assertEquals(MaterialsViewModel().updateData(wall.materialList().materials()).data, test.materials.data)
        assertEquals(GraphicsViewModel().updateData(wall.graphicsList().drawingInstructions()).data, test.graphics.data)
    }
}
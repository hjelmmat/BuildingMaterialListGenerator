package ui.models.wall

import ui.models.graphics.GraphicsViewModel
import ui.models.materials.MaterialsViewModel
import java.util.Vector

class WallContentsViewModel {
    val materials = MaterialsViewModel()
    val graphics = GraphicsViewModel()

    fun update(material: Vector<Vector<String>>, graphic: Vector<Vector<Vector<Int>>>) {
        materials.updateData(material)
        graphics.updateData(graphic)
    }
}

package ui.models.wall

import ui.models.graphics.GraphicsViewModel
import ui.models.materials.MaterialsViewModel
import java.util.Vector

class WallContentsViewModel {
    val materials = MaterialsViewModel()
    val graphics = GraphicsViewModel()

    fun update(material: HashMap<String, Vector<Vector<String>>>, graphic: HashMap<String, Vector<Vector<Int>>>) {
        materials.updateData(material)
        graphics.updateData(graphic)
    }
}

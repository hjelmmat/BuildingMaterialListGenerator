package graphics

import models.Measurement
import java.util.*
import kotlin.collections.HashMap

/**
 * Stores lists of drawing instructions for lines and rectangles.
 */
class GraphicsList {
    private val instructions: Vector<GraphicsInstructions> = Vector()

    /**
     *
     * @return A Vector of Vectors of Instructions. The first element is for lines. The second element is for rectangles
     */
    fun drawingInstructions(): HashMap<String, Vector<Vector<Int>>> {
        val result = HashMap<String, Vector<Vector<Int>>>()
        val lines = Vector<Vector<Int>>()
        val rectangles = Vector<Vector<Int>>()
        for (graphic in this.instructions) {
            val instructions = graphic.drawingInstructions()
            if (graphic is LineInstructions) {
                lines.add(instructions)
            } else if (graphic is RectangleInstructions) {
                rectangles.add(instructions)
            }
        }
        if (!lines.isEmpty()) {
            result["lines"] = lines
        }
        if (!rectangles.isEmpty()) {
            result["rectangles"] = rectangles
        }
        return result
    }

    /**
     *
     * @param graphic - add a graphic to this list
     * @return this list
     */
    fun addGraphic(graphic: GraphicsInstructions): GraphicsList {
        instructions.add(graphic)
        return this
    }

    /**
     *
     * @param drawingInstructions - A GraphicsList to merge with this one
     * @return this GraphicsList
     */
    fun addGraphics(drawingInstructions: GraphicsList): GraphicsList {
        instructions.addAll(drawingInstructions.instructions)
        return this
    }

    /**
     *
     * @param horizontal - how far to shift horizontally
     * @param vertical - how far to shift vertically
     * @return this GraphicsList
     */
    fun shift(horizontal: Measurement, vertical: Measurement): GraphicsList {
        val result = GraphicsList()
        instructions.forEach() { result.addGraphic(it.shift(horizontal, vertical)) }
        return result
    }
}
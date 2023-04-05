package graphics

import models.Measurement
import java.util.*

/**
 * A Class used to store information required to draw a rectangle
 * @param topLeftX - The x position of the top left corner of the rectangle
 * @param topLeftY - The y position of the top left corner of the rectangle
 * @param width - the width of the rectangle
 * @param height - the height of the rectangle
 */
class RectangleInstructions(
    private val topLeftX: Measurement,
    private val topLeftY: Measurement,
    private val width: Measurement,
    private val height: Measurement
) : GraphicsInstructions {

    /**
     *
     * @return A Vector of Integers (pixels) required to create this Rectangle
     */
    override fun drawingInstructions(): Vector<Int> {
        return Vector(
            listOf(
                this.topLeftX.numberOfPixels,
                this.topLeftY.numberOfPixels,
                this.width.numberOfPixels,
                this.height.numberOfPixels,
            )
        )
    }

    /**
     * Shifts the Rectangle top left corner horizontally and vertically
     * @param horizontal - How far to shift the Rectangle horizontally
     * @param vertical - How far to shift the Rectangle vertically
     * @return this Rectangle
     */
    override fun shift(horizontal: Measurement, vertical: Measurement): RectangleInstructions {
        return RectangleInstructions(topLeftX.add(horizontal), topLeftY.add(vertical), width, height)
    }
}
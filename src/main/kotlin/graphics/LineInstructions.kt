package graphics

import models.Measurement
import java.util.*

/**
 * A Class used to store information required to draw a line
 * @param startX - Start x position of the line
 * @param startY - Start y position of the line
 * @param endX - End x position of the line
 * @param endY - End y position of the line
 */
class LineInstructions(
    private val startX: Measurement,
    private val startY: Measurement,
    private val endX: Measurement,
    private val endY: Measurement
) : GraphicsInstructions {

    /**
     *
     * @return A Vector of Integers (pixels) required to create this Line
     */
    override fun drawingInstructions(): Vector<Int> {
        return Vector(
            listOf(
                this.startX.numberOfPixels,
                this.startY.numberOfPixels,
                this.endX.numberOfPixels,
                this.endY.numberOfPixels,
            )
        )
    }

    /**
     * Shifts the Line start and end point horizontally and vertically
     * @param horizontal - How far to shift the line horizontally
     * @param vertical - How far to shift the line vertically
     * @return this Line
     */
    override fun shift(horizontal: Measurement, vertical: Measurement): GraphicsInstructions {
        return LineInstructions(startX.add(horizontal), startY.add(vertical), endX.add(horizontal), endY.add(vertical))
    }
}
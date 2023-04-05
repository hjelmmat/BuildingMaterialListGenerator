package graphics

import models.Measurement
import java.util.*

interface GraphicsInstructions {
    fun drawingInstructions(): Vector<Int>
    fun shift(horizontal: Measurement, vertical: Measurement): GraphicsInstructions
}
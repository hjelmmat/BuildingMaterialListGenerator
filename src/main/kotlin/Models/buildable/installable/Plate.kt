package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.material.Lumber
import models.Measurement
import kotlin.math.ceil

/**
 * A Stud that is installed horizontally is a plate. Typically installed with nails at the start and end of the plate
 * as well as every 12 inches.
 * @param length    - Length of plate
 * @param dimension - dimension of Lumber to use for plate
 */
class Plate(length: Measurement, dimension: Lumber.Dimension) : Stud(length, dimension) {
    // Plates should be installed with a pair of nails every 12" with a set at the beginning and the end.
    override fun numberOfNails() = numberOfNails(totalWidth())

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Plate
     */
    override fun totalWidth(): Measurement {
        return super.totalHeight()
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Plate
     */
    override fun totalHeight(): Measurement {
        return super.totalWidth()
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Plate (as a
     * sideways [Stud])
     */
    override fun graphicsList(): GraphicsList {
        val zero = Measurement(0)
        return GraphicsList().addGraphic(RectangleInstructions(zero, zero, totalWidth(), totalHeight()))
    }

    companion object {
        /**
         * Other studs may be attached like plates without being plates themselves. This allows for use of the same
         * calculation.
         * @param length - Length of the plate to find nails for
         * @return The number of nails to use
         */
        @JvmStatic
        fun numberOfNails(length: Measurement): Int {
            val nailSpacing = Measurement(12)
            val numberOfNailPairs = ceil(length.divide(nailSpacing)).toInt() + 1
            return numberOfNailPairs * 2
        }
    }
}
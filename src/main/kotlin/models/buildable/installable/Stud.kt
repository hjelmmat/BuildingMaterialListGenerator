package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import models.Measurement

/**
 * Class used to describe a stud, essentially a c-style struct
 * @param installedHeight - The height of the stud
 * @param dimension - the width/height dimension of the stud.
 */
open class Stud(
    private val installedHeight: Measurement = Measurement(92, Measurement.Fraction.FIVE_EIGHTH),
    internal val dimension: Lumber.Dimension = Lumber.Dimension.TWO_BY_FOUR
) : Installable {
    init {
        val maxLength = Lumber.FactoryLength.maxLength
        if (installedHeight > maxLength) {
            throw InvalidLengthException("cannot be longer than ${Lumber.FactoryLength.maxLength}", maxLength)
        }
    }

    protected val material: MaterialList = MaterialList().addMaterial(Lumber(installedHeight, dimension), 1)
        .addMaterial(nailType, this.numberOfNails())

    protected open fun numberOfNails() = 6

    /**
     * Studs are only equal if the installedHeight is the same, which will also have the same FactoryLength for the
     * MaterialList
     * @param other - the object to be compared
     * @return Indication if the two studs are equal
     */
    override fun equals(other: Any?) = (other === this)
            || (other is Stud && this.hashCode() == other.hashCode())

    /**
     *
     * @return hash code based on the dimension, installed length, and class name. Class name is necessary to be
     * different from other classes with dimension and installation length.
     */
    override fun hashCode(): Int {
        return (dimension.toString() + installedHeight.toString() + this.javaClass.name).hashCode()
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Stud
     */
    override fun totalWidth(): Measurement {
        return dimension.width
    }

    /**
     *
     * @return - A copy of the [Measurement] of the height of this Stud
     */
    override fun totalHeight(): Measurement {
        return installedHeight
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this Stud
     */
    override fun materialList(): MaterialList {
        return material
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Stud
     */
    override fun graphicsList(): GraphicsList {
        val zero = Measurement(0)
        return GraphicsList().addGraphic(RectangleInstructions(zero, zero, dimension.width, installedHeight))
    }

    class InvalidLengthException(s: String, val maxLength: Measurement) : IllegalArgumentException(s)

    companion object {
        val nailType = Nail.TEN_D
    }
}
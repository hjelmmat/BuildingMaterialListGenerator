package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.material.Lumber
import models.Measurement

/**
 * Frequently, studs are place right next to each other and that requires them to be nailed together. This class ensures
 * the material list and drawing instructions are updated accordingly.
 * @param length - Length of Double stud to create
 * @param dimension - Dimension of one of the studs to create the doubleStud
 */
open class DoubleStud(length: Measurement, dimension: Lumber.Dimension) : Stud(length, dimension) {
    init {
        // The second stud is attached like a plate, so use the Plate static method here.
        material.addMaterial(nailType, Plate.numberOfNails(length))
        material.addMaterial(Lumber(length, dimension), 1)
    }

    /**
     * A Convenience constructor for making double studs from a stud
     * @param stud - Stud to create a double stud from
     */
    constructor(stud: Stud) : this(stud.totalHeight(), stud.dimension)

    /**
     *
     * @return - A copy of the [Measurement] of the width of this DoubleStud
     */
    override fun totalWidth(): Measurement {
        return super.totalWidth().multiply(2)
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this DoubleStud
     */
    override fun graphicsList(): GraphicsList {
        val result = super.graphicsList()
        val zero = Measurement(0)
        return result.addGraphic(RectangleInstructions(super.totalWidth(), zero, super.totalWidth(), totalHeight()))
    }
}
package models.buildable.installable

import graphics.GraphicsList
import models.buildable.material.MaterialList
import models.Measurement

interface Installable {
    /**
     *
     * @return - A copy of the [Measurement] of the width of this Installable
     */
    fun totalWidth(): Measurement

    /**
     *
     * @return - A copy of the [Measurement] of the height of the installable
     */
    fun totalHeight(): Measurement

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this Installable
     */
    fun materialList(): MaterialList

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Installable
     */
    fun graphicsList(): GraphicsList
}
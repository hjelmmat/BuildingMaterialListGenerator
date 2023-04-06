package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.buildable.material.Nail
import models.Measurement

/**
 * A Header is a load bearing combination of studs to support weight above openings in wall, like doors and windows
 * @param gapWidth - The size of the gap to cover with the Header. This includes the width of the studs the header
 * is placed on top of.
 */
class Header(gapWidth: Measurement) : Installable {
    private val materials = MaterialList()
    private val graphics = GraphicsList()
    private val width = gapWidth
    private val height: Measurement

    init {
        val loadBearingDimension = Lumber.Dimension.TWO_BY_SIX
        val loadBearingStud = DoubleStud(gapWidth, loadBearingDimension)
        val plateDimension = Lumber.Dimension.TWO_BY_FOUR
        val plateStuds = Plate(gapWidth, plateDimension)
        this.materials.addMaterials(loadBearingStud.materialList())
            // Both the top and bottom plates need to be attached to the header
            .addMaterials(plateStuds.materialList())
            .addMaterials(plateStuds.materialList())
            // Generally, plates don't need to be attached twice, but in this case, it should be attached to the
            // loadBearingStud like a plate and to the vertical studs with toenails.
            .addMaterial(Nail.TEN_D, 12)
        val zero = Measurement(0)
        // Add the
        this.graphics
            .addGraphics(plateStuds.graphicsList())
            .addGraphic(RectangleInstructions(zero, plateDimension.width, gapWidth, loadBearingDimension.height))
            .addGraphic(
                RectangleInstructions(
                    zero,
                    plateDimension.width.add(loadBearingDimension.height),
                    gapWidth,
                    plateDimension.width
                )
            )
        height = loadBearingDimension.height.add(plateDimension.width.multiply(2))
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Header
     */
    override fun totalWidth(): Measurement {
        return width
    }

    /**
     *
     * @return - A copy of the [Measurement] of the height of this Header
     */
    override fun totalHeight(): Measurement {
        return height
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this Header
     */
    override fun materialList(): MaterialList {
        return materials
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Header
     */
    override fun graphicsList(): GraphicsList {
        return graphics
    }
}
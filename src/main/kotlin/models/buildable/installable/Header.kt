package models.buildable.installable

import graphics.GraphicsList
import graphics.RectangleInstructions
import models.buildable.material.MaterialList
import models.Measurement
import models.buildable.material.Nail

/**
 * A Header is a load bearing combination of studs to support weight above openings in wall, like doors and windows
 * @param loadBearingBeam - The studs that bears of the load of the header
 * @param plateStud - The studs that go on top and bottom of the loadBearingStud
 *
 * The Header class also calculates the trimmer studs necessary to hold up the header
 */
class Header(loadBearingBeam: Stud, plateStud: Plate) : Installable {
    private val width: Measurement
    private val height: Measurement
    private val materials: MaterialList
    private val graphics: GraphicsList

    init {
        require(loadBearingBeam.totalHeight() == plateStud.totalWidth()) {
            "LoadBearing stud and Plate must be the same length"
        }
        width = plateStud.totalWidth()
        height = plateStud.totalHeight().multiply(2).add(loadBearingBeam.dimension.height)
        materials = MaterialList()
            .addMaterials(DoubleStud(loadBearingBeam).materialList())
            .addMaterials(plateStud.materialList())
            .addMaterials(plateStud.materialList())
            // The plates need to be attached to the loadBearingDoubleStud like plates, but also should be attached
            // to the sides like a stud, adding extra toenails here
            .addMaterial(Nail.TEN_D, 12)
        graphics = GraphicsList()
            .addGraphics(plateStud.graphicsList())
            .addGraphic(
                RectangleInstructions(
                    Measurement(0),
                    plateStud.totalHeight(),
                    loadBearingBeam.totalHeight(),
                    loadBearingBeam.dimension.height,
                )
            )
            .addGraphics(plateStud.graphicsList().shift(Measurement(0), this.height.subtract(plateStud.totalHeight())))
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
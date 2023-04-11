package models.buildable.installable

import graphics.GraphicsList
import models.Measurement
import models.buildable.material.MaterialList

class Window(
    private val windowWidth: Measurement,
    windowHeight: Measurement,
    private val bottomOfWindowHeight: Measurement,
    totalOpeningHeight: Measurement
) :
    Opening(width = windowWidth, height = windowHeight.add(bottomOfWindowHeight), totalHeight = totalOpeningHeight) {
    private val floorCrippleStud: Stud?
    private val floorCrippleLayout: CrippleLayout?
    private val floorStudHorizontalShift = studDimension.width.multiply(2)
    private val plate = Plate(windowWidth, this.studDimension)

    init {
        if (bottomOfWindowHeight != Measurement(0)) {
            floorCrippleStud = Stud(bottomOfWindowHeight.subtract(studDimension.width), studDimension)
            floorCrippleLayout = CrippleLayout(windowWidth, floorCrippleStud)
        } else {
            floorCrippleStud = null
            floorCrippleLayout = null
        }
    }

    override fun addCrippleStud(locationInOpening: Measurement): Opening {
        super.addCrippleStud(locationInOpening)
        floorCrippleLayout?.addStudAt(locationInOpening.subtract(floorStudHorizontalShift), floorCrippleStud!!)
        return this
    }

    override fun graphicsList(): GraphicsList {
        val result = super.graphicsList()
        floorCrippleLayout?.let {
            val verticalPlateShift = this.totalHeight().subtract(bottomOfWindowHeight)
            result
                // Only add a plate if there are cripple studs. Otherwise, the window is resting on the bottom plate
                // of the wall (or whatever)
                .addGraphics(plate.graphicsList().shift(floorStudHorizontalShift, verticalPlateShift))
                .addGraphics(
                    it.graphicsList().shift(floorStudHorizontalShift, verticalPlateShift.add(studDimension.width))
                )
        }
        return result
    }

    override fun materialList(): MaterialList {
        val result = super.materialList()
            // The plate is attached as a stud, so we need to include that material here
            .addMaterials(Stud(windowWidth, studDimension).materialList())
        floorCrippleLayout?.let {
            result.addMaterials(it.materialList())
        }
        return result
    }
}
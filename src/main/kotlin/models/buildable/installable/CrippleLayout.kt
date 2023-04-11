package models.buildable.installable

import models.Measurement
import models.buildable.material.MaterialList

/**
 * A class to create a layout for studs that are between a set of studs. Generally used above headers. The layout
 * dimensions should be known when constructed as well as the stud type
 */
class CrippleLayout(width: Measurement, studType: Stud) : Layout() {
    // The first and last studs will be attached to the outside studs and so need to have extra nails to be attached
    private var extraNails = Plate.numberOfNails(studType.totalHeight()) * 2
    private val lastStud = width.subtract(studType.totalWidth())

    init {
        super.addStudAt(Measurement(0), studType)
        super.addStudAt(lastStud, studType)
    }

    override fun addStudAt(position: Measurement, stud: Stud): CrippleLayout {
        // If a stud would exactly line up with the first or last studs, it can be ignored
        if (position == Measurement(0) || position == lastStud) {
            return this
        }
        try {
            super.addStudAt(position, stud)

        } catch (e: InstallableLocationConflict) {
            /*
           There should only be two situations here:
            1. A stud that conflicts with the first or last one, then it needs to be added but shifted out of the way
            2. A stud that conflicts with any other stud, then the exception is valid and should be thrown again.
            A cripple stud interfering with the king stud should be handled above
            */
            when (e.conflict) {
                Measurement(0) -> {
                    this.studs.remove(e.conflict)
                    super.addStudAt(e.conflict, DoubleStud(stud))
                }

                lastStud -> {
                    this.studs.remove(e.conflict)
                    super.addStudAt(lastStud.subtract(stud.totalWidth()), DoubleStud(stud))
                }

                else -> {
                    throw e
                }
            }
        }
        return this
    }

    override fun materialList(): MaterialList {
        return super.materialList().addMaterial(Stud.nailType, this.extraNails)
    }
}
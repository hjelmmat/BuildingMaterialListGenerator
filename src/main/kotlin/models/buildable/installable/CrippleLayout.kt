package models.buildable.installable

import models.Measurement
import models.buildable.material.MaterialList

/**
 * A class to create a layout for studs that are between a set of studs. Generally used above headers. The layout
 * dimensions should be known when constructed as well as the stud type. If any cripple studs are added before or after
 * the first and last studs, it will be ignored
 */
class CrippleLayout(
    private val firstStudPosition: Measurement,
    private val lastStudPosition: Measurement,
    studType: Stud
) : Layout() {
    // The first and last studs will be attached to the outside studs and so need to have extra nails to be attached
    private var extraNails = Plate.numberOfNails(studType.totalHeight()) * 2

    init {
        super.addStudAt(firstStudPosition, studType)
        super.addStudAt(lastStudPosition, studType)
    }

    override fun addStudAt(position: Measurement, stud: Stud): CrippleLayout {
        // If a stud would exactly line up with the first or last studs, it can be ignored.
        if ((position <= firstStudPosition || position >= lastStudPosition)) {
            return this
        } else if (position == studs.firstKey().add(studs.firstEntry().value.totalWidth())) {
            updateStudToDouble(studs.firstKey())
        } else if (position == studs.lastKey().subtract(studs.lastEntry().value.totalWidth())) {
            updateStudToDouble(studs.lastKey(), position)
        } else {
            try {
                super.addStudAt(position, stud)
            } catch (e: InstallableLocationConflict) {
                /*
               There should only be two situations here:
                1. A stud that conflicts with the first or last one, then it needs to be added but shifted out of the way
                2. A stud that conflicts with any other stud, then the exception is valid and should be thrown again.
                */
                when (e.conflict) {
                    firstStudPosition -> {
                        updateStudToDouble(e.conflict, e.conflict)
                    }

                    lastStudPosition -> {
                        updateStudToDouble(e.conflict, lastStudPosition.subtract(stud.totalWidth()))
                    }

                    else -> {
                        throw e
                    }
                }
            }
        }
        return this
    }

    private fun updateStudToDouble(oldLocation: Measurement, newLocation: Measurement = oldLocation) {
        this.studs[oldLocation]?.let {
            this.studs.remove(oldLocation)
            super.addStudAt(newLocation, DoubleStud(it))
        }
    }

    override fun materialList(): MaterialList {
        return super.materialList().addMaterial(Stud.nailType, this.extraNails)
    }
}
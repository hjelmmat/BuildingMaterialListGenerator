package models.buildable.installable

import graphics.GraphicsList
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.Measurement

/**
 * A class designed to store information about a door
 */
class Door constructor(private val type: StandardDoor = StandardDoor.Bedroom) : Installable {
    private val studDimension = Lumber.Dimension.TWO_BY_FOUR

    // Headers should be long enough to cover the gap and rest on two trimmers, which are 2x4s
    private val header: Header = Header(type.openingWidth.add(studDimension.width.multiply(2)))
    private val topOfHeader: Measurement = header.totalHeight().add(type.openingHeight)

    // Total height starts at the top of the header but might be larger as the cripple studs are added
    private var totalHeight = topOfHeader

    // there will be 2 king and 2 jack studs all of 2x4 so that is part of the total width.
    private val totalWidth: Measurement = type.openingWidth.add(studDimension.width.multiply(4))
    private var floorLayout = this.floorLayout()
    private val crippleStuds = Layout()

    // Extra nails are needed to attach trimmers to kings and the edge cripple studs to kings.
    private var extraNails = Plate.numberOfNails(type.openingHeight) * 2

    private fun floorLayout(): Layout {
        val king = Stud(this.totalHeight, this.studDimension)
        val trimmer = Stud(type.openingHeight, this.studDimension)
        return Layout()
            .addStudAt(Measurement(0), king)
            .addStudAt(this.studDimension.width, trimmer)
            .addStudAt(totalWidth.subtract(this.studDimension.width.multiply(2)), trimmer)
            .addStudAt(totalWidth.subtract(this.studDimension.width), king)
    }

    /**
     *
     * @return - A copy of the [Measurement] of the height of this Door
     */
    override fun totalHeight(): Measurement {
        return totalHeight
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Door
     */
    override fun totalWidth(): Measurement {
        return totalWidth
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this Door
     */
    override fun materialList(): MaterialList {
        return MaterialList()
            .addMaterials(floorLayout.materialList())
            .addMaterials(header.materialList())
            .addMaterials(crippleStuds.materialList())
            .addMaterial(Stud.nailType, extraNails)
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Door
     */
    override fun graphicsList(): GraphicsList {
        return GraphicsList()
            .addGraphics(floorLayout.graphicsList())
            .addGraphics(header.graphicsList().shift(studDimension.width, totalHeight.subtract(topOfHeader)))
            .addGraphics(crippleStuds.graphicsList())
    }

    /**
     * This method is intending to take a stud that was in a wall layout and move it into the Door's layout instead.
     * If the stud happens to conflict with a King stud of the Door, it will be moved next to it.
     * @param stud - A stud replaced by this door
     * @param locationInDoor - The location of the stud relative to the start of the door
     * @return This Door
     */
    @Throws(InstallableLocationConflict::class)
    fun addCrippleStud(stud: Stud, locationInDoor: Measurement): Door {
        if (locationInDoor > this.totalWidth) {
            val error = "Stud cannot be added at $locationInDoor. Door is only ${this.totalWidth} long"
            throw InstallableLocationConflict(error, locationInDoor)
        }

        // If the cripple stud need to go before the first or after the last, it can be ignored as there is a king stud
        // there already
        val firstCrippleStud = this.studDimension.width
        val lastCrippleStud = totalWidth().subtract(this.studDimension.width.multiply(2))
        if (locationInDoor <= firstCrippleStud || locationInDoor >= lastCrippleStud) {
            return this
        }

        // The king studs need to be updated to be taller
        if (stud.totalHeight() > totalHeight) {
            totalHeight = stud.totalHeight()
            floorLayout = floorLayout()
        }

        // Cripple studs should be as tall to take the space between the open
        val crippleStudHeight = totalHeight().subtract(topOfHeader)
        val crippleStud = Stud(crippleStudHeight, studDimension)

        // If there are any cripple studs that end up next to each other, the number of extra nails that need to be added
        // is below
        val crippleStudExtraNails: Int = Plate.numberOfNails(crippleStudHeight)
        // There needs to be one above each trimmer
        if (crippleStuds.isEmpty) {
            crippleStuds.addStudAt(firstCrippleStud, crippleStud)
            crippleStuds.addStudAt(lastCrippleStud, crippleStud)

            // These studs are attached to the king studs on the outside in the same manner as a plate, but are not
            // actually plates, so we need to manually add the extra nails
            extraNails += crippleStudExtraNails * 2
        }
        try {
            crippleStuds.addStudAt(locationInDoor, crippleStud)

        } catch (e: InstallableLocationConflict) {
            /*
           There should only be two situations here:
            1. A stud that conflicts with the first or last one, then it needs to be added but shifted out of the way
            2. A stud that conflicts with any other stud, then the exception is valid and should be thrown again.
            A cripple stud interfering with the king stud should be handled above
            */
            extraNails += when (e.conflict) {
                firstCrippleStud -> {
                    crippleStuds.addStudAt(firstCrippleStud.add(crippleStud.totalWidth()), crippleStud)
                    crippleStudExtraNails
                }

                lastCrippleStud -> {
                    crippleStuds.addStudAt(lastCrippleStud.subtract(crippleStud.totalWidth()), crippleStud)
                    crippleStudExtraNails
                }

                else -> {
                    throw e
                }
            }
        }
        return this
    }

    /**
     * Doors are only equal if the type of door and the tripe layout are the same
     * @param other - the object to be compared
     * @return Indication if the two doors are equal
     */
    override fun equals(other: Any?) = (other === this)
            || (other is Door && type == other.type && crippleStuds == other.crippleStuds)

    /**
     *
     * @return hash code based on the type and tripleLayout
     */
    override fun hashCode() = type.hashCode() + crippleStuds.hashCode()

    /**
     * Width and Heights of doors are inclusive of the lumber used to FRAME a door, not the final installed door. This
     * means a Standard Bedroom door, which has an 80" high installed door, actually requires 88 1/2" of framed space.
     * This is also true of the width.
     */
    enum class StandardDoor(width: Measurement, height: Measurement) {
        Bedroom(Measurement(36), Measurement(80));

        val openingWidth = Measurement(2).add(width)
        val openingHeight = Measurement(1).add(height)
    }
}
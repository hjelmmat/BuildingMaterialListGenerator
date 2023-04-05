package models.buildable.installable

import graphics.GraphicsList
import models.buildable.material.MaterialList
import models.Measurement
import models.Measurement.InvalidMeasurementException
import java.util.*

/**
 * Class used to describe positions of [Installable]. Essentially a c-style struct
 */
class Layout : Installable {
    private val studs: TreeMap<Measurement, Stud> = TreeMap()
    private val doors: TreeMap<Measurement, Door> = TreeMap()
    val isEmpty: Boolean
        get() {
            return studs.isEmpty() && doors.isEmpty()
        }
    private var tallestStudHeight = Measurement(0)

    /**
     * An exception that is used when an [Installable] cannot be placed where requested
     */
    class InstallableLocationConflict(s: String, val conflict: Measurement) : IllegalArgumentException(s)

    /**
     *
     * @param position - Position of the stud in the layout
     * @param stud - Stud that goes at the position
     * @return Updated Layout with new stud
     */
    fun addStudAt(position: Measurement, stud: Stud): Layout {
        detectConflicts(position, stud.totalWidth().add(position), studs, "Stud")
        studs[position] = stud

        // Keep track of the tallest studs, so we know how far to shift studs down in the graphics
        tallestStudHeight = if (tallestStudHeight < stud.totalHeight()) stud.totalHeight() else tallestStudHeight
        return this
    }

    /**
     * Add a door to this layout. If the door position conflicts with any studs on the right or left edge, then it will
     * move those studs outside the door to retain proper layout minimum distances.
     * @param ofType - The type of [models.buildable.installable.Door.StandardDoor] to add
     * @param atLocation - the Location to add the door
     * @return - Updated Layout with new Door
     * @throws InstallableLocationConflict - When the new door interacts with an already added Door or if a stud cannot
     * be moved
     */
    @Throws(InstallableLocationConflict::class)
    fun addDoorAt(ofType: Door, atLocation: Measurement): Layout {
        val doorEndLocation = atLocation.add(ofType.totalWidth())
        if (doorEndLocation > totalWidth()) {
            val error =
                "Door cannot be added at $atLocation. Layout is only ${totalWidth()} long and door ends at $doorEndLocation"
            throw InstallableLocationConflict(error, atLocation)
        }

        detectConflicts(atLocation, doorEndLocation, this.doors, "Door")
        tryShiftOutsideStudsToValidPosition(atLocation, doorEndLocation, "Door")

        val removedKeys = Vector<Measurement>()
        studs.subMap(atLocation, doorEndLocation).forEach {
            ofType.addCrippleStud(it.value, it.key.subtract(atLocation))
            removedKeys.add(it.key)
        }
        removedKeys.forEach { studs.remove(it) }
        doors[atLocation] = ofType
        return this
    }

    /*
    If a stud is left of the start location then shift it farther left
    If a stud extends to the right outside the location from the inside, then shift it farther right
    This method is recursive to handle both the start and end locations
     */
    @Throws(InvalidMeasurementException::class)
    private fun tryShiftOutsideStudsToValidPosition(
        startLocation: Measurement,
        endLocation: Measurement,
        installableType: String
    ) {
        try {
            detectConflicts(startLocation, endLocation, this.studs, "studs")
        } catch (c: InstallableLocationConflict) {
            val conflictLocation = c.conflict
            val studNeedingMoving = this.studs[conflictLocation]!!
            val error = "$installableType cannot be added at $startLocation. Stud at $conflictLocation cannot be moved."
            val potentialException = InstallableLocationConflict(error, conflictLocation)

            /*
            Shift Stud Left
             */
            if (conflictLocation < startLocation) {
                val updatedLocation: Measurement = try {
                    startLocation.subtract(studNeedingMoving.totalWidth())
                } catch (e: InvalidMeasurementException) {
                    throw potentialException
                }
                this.studs.remove(conflictLocation)
                addStudAt(updatedLocation, studNeedingMoving)

                // Try again since it might have multiple conflicts
                tryShiftOutsideStudsToValidPosition(startLocation, endLocation, installableType)
            } else if (studNeedingMoving.totalWidth().add(endLocation) > totalWidth()) {
                throw potentialException
            } else if (studNeedingMoving.totalWidth().add(conflictLocation) > endLocation) {
                this.studs.remove(conflictLocation)
                addStudAt(endLocation, studNeedingMoving)
            }
            /*
            Otherwise, do nothing
             */
        }
    }

    @Throws(InstallableLocationConflict::class)
    private fun <T : Installable> detectConflicts(
        startLocation: Measurement,
        endLocation: Measurement,
        currentInstallables: TreeMap<Measurement, T>,
        installableType: String,
    ) {
        /*
         Check to see if this location conflicts with any other installables assuming the installable with conflict
         with either the left or right edge
         */
        val error = "$installableType cannot be added at $startLocation. $installableType already is located at "
        val leftOutside = currentInstallables.floorKey(startLocation)
        if (leftOutside is Measurement
            && currentInstallables[leftOutside]!!.totalWidth().add(leftOutside) > startLocation
        ) {
            throw InstallableLocationConflict(error + leftOutside, leftOutside)
        }
        val rightInside = currentInstallables.floorKey(endLocation)
        if (rightInside is Measurement
            && rightInside >= startLocation && rightInside < endLocation
        ) {
            throw InstallableLocationConflict(error + rightInside, rightInside)
        }
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Layout
     */
    override fun totalWidth(): Measurement {
        val lastStud = studs.lastEntry()
        val greatestStudWidth = lastStud?.key?.add(lastStud.value.totalWidth()) ?: Measurement(0)
        val lastDoor = doors.lastEntry()
        val greatestDoorWidth = lastDoor?.key?.add(lastDoor.value.totalWidth()) ?: Measurement(0)
        return if (greatestStudWidth > greatestDoorWidth) greatestStudWidth else greatestDoorWidth
    }

    override fun totalHeight(): Measurement {
        return tallestStudHeight
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this Layout
     */
    override fun materialList(): MaterialList {
        val result = MaterialList()
        studs.values.forEach { result.addMaterials(it.materialList()) }
        doors.values.forEach { result.addMaterials(it.materialList()) }
        return result
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Layout
     */
    override fun graphicsList(): GraphicsList {
        val result = GraphicsList()
        studs.forEach { (location: Measurement, stud: Stud) ->
            result.addGraphics(stud.graphicsList().shift(location, tallestStudHeight.subtract(stud.totalHeight())))
        }
        doors.forEach { (location: Measurement, door: Door) ->
            result.addGraphics(door.graphicsList().shift(location, Measurement(0)))
        }
        return result
    }

    /**
     *
     * @param other - the object to compare to this
     * @return if the object is the same or not
     */
    override fun equals(other: Any?) = (other === this)
            || other is Layout && this.studs == other.studs && this.doors == other.doors

    /**
     *
     * @return a hashcode for this Layout
     */
    override fun hashCode(): Int {
        return studs.hashCode() * doors.hashCode()
    }
}
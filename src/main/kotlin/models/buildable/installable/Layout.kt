package models.buildable.installable

import graphics.GraphicsList
import models.buildable.material.MaterialList
import models.Measurement
import models.Measurement.InvalidMeasurementException
import java.util.*

/**
 * Class used to describe positions of [Installable]. Essentially a c-style struct
 */
open class Layout : Installable {
    protected val studs: TreeMap<Measurement, Stud> = TreeMap()
    private val openings: TreeMap<Measurement, Opening> = TreeMap()
    val isEmpty: Boolean
        get() {
            return studs.isEmpty() && openings.isEmpty()
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
    open fun addStudAt(position: Measurement, stud: Stud): Layout {
        detectConflicts(position, stud.totalWidth().add(position), studs, "Stud")
        studs[position] = stud

        // Keep track of the tallest studs, so we know how far to shift studs down in the graphics
        tallestStudHeight = if (tallestStudHeight < stud.totalHeight()) stud.totalHeight() else tallestStudHeight
        return this
    }

    /**
     * Add an opening to this layout. If the opening position conflicts with any studs on the right or left edge, then it
     * will move those studs outside the opening to retain proper layout minimum distances.
     * @param ofType - The type of [models.buildable.installable.StandardDoor] to add
     * @param atLocation - the Location to add the opening
     * @return - Updated Layout with new opening
     * @throws InstallableLocationConflict - When the new opening interacts with an already added opening or if a stud
     * cannot be moved
     */
    @Throws(InstallableLocationConflict::class)
    fun addOpeningAt(ofType: Opening, atLocation: Measurement): Layout {
        val endLocation = atLocation.add(ofType.totalWidth())
        if (endLocation > totalWidth()) {
            val error =
                "Cannot be added at $atLocation, is only ${totalWidth()} wide and opening ends at $endLocation"
            throw InstallableLocationConflict(error, atLocation)
        }

        try {
            detectConflicts(atLocation, endLocation, this.openings, "Opening")
        } catch (e: InstallableLocationConflict) {
            openings[e.conflict]!!.addUpperOpening(ofType, atLocation.subtract(e.conflict))
            return this
        }

        tryShiftOutsideStudsToValidPosition(atLocation, endLocation, "Opening")

        openings[atLocation] = ofType

        val studsToRemove = mutableListOf<Measurement>()
        studs.subMap(atLocation, endLocation).forEach {
            ofType.addCrippleStud(it.key.subtract(atLocation))
            studsToRemove.add(it.key)
        }
        studsToRemove.forEach { studs.remove(it) }
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
            val error = "Cannot be added at $startLocation. Stud at $conflictLocation cannot be moved."
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
            } else if (conflictLocation == startLocation || conflictLocation == endLocation) {
                // If the stud is exactly aligned with the outside stud, remove it since it will be replaced
                this.studs.remove(conflictLocation)
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
        val error = "Cannot be added at $startLocation, collides with $installableType at "
        currentInstallables.floorKey(startLocation)?.let { leftOutside: Measurement ->
            currentInstallables[leftOutside]?.let { installable ->
                if (installable.totalWidth().add(leftOutside) > startLocation) {
                    throw InstallableLocationConflict(error + leftOutside, leftOutside)
                }
            }
        }
        currentInstallables.floorKey(endLocation)?.let { rightInside: Measurement ->
            if (rightInside >= startLocation && rightInside < endLocation) {
                throw InstallableLocationConflict(error + rightInside, rightInside)
            }
        }
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Layout
     */
    override fun totalWidth(): Measurement {
        val lastStud = studs.lastEntry()
        val greatestStudWidth = lastStud?.key?.add(lastStud.value.totalWidth()) ?: Measurement(0)
        val lastOpening = openings.lastEntry()
        val greatestOpeningWidth = lastOpening?.key?.add(lastOpening.value.totalWidth()) ?: Measurement(0)
        return if (greatestStudWidth > greatestOpeningWidth) greatestStudWidth else greatestOpeningWidth
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
        openings.values.forEach { result.addMaterials(it.materialList()) }
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
        openings.forEach { (location: Measurement, opening: Opening) ->
            result.addGraphics(opening.graphicsList().shift(location, Measurement(0)))
        }
        return result
    }

    /**
     *
     * @param other - the object to compare to this
     * @return if the object is the same or not
     */
    override fun equals(other: Any?) = (other === this)
            || (other is Layout && graphicsList() == other.graphicsList() && materialList() == other.materialList())

    /**
     *
     * @return a hashcode for this Layout
     */
    override fun hashCode(): Int {
        return graphicsList().hashCode() * materialList().hashCode()
    }
}
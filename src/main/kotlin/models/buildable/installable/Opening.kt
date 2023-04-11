package models.buildable.installable

import graphics.GraphicsList
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.Measurement

/**
 * A class designed to store information about an opening
 */
open class Opening(val width: Measurement, val height: Measurement, private val totalHeight: Measurement) :
    Installable {
    protected val studDimension = Lumber.Dimension.TWO_BY_FOUR

    // Headers should be long enough to cover the gap and rest on two trimmers, which are 2x4s
    private val header: Header = Header(width.add(studDimension.width.multiply(2)))
    private val topOfHeader: Measurement = header.totalHeight().add(height)

    // there will be 2 king and 2 jack studs all of 2x4 so that is part of the total width.
    private val totalWidth: Measurement = width.add(studDimension.width.multiply(4))
    private val floorLayout: Layout
    private val crippleStud: Stud?
    private val crippleLayout: CrippleLayout?

    // Extra nails are needed to attach trimmers to kings and the edge cripple studs to kings.
    private var extraNails = Plate.numberOfNails(height) * 2

    init {
        require(totalHeight >= topOfHeader) {
            "Total height of $totalHeight is too short, requires $topOfHeader"
        }
        val king = Stud(totalHeight, this.studDimension)
        val trimmer = Stud(height, this.studDimension)
        floorLayout = Layout()
            .addStudAt(Measurement(0), king)
            .addStudAt(this.studDimension.width, trimmer)
            .addStudAt(totalWidth.subtract(this.studDimension.width.multiply(2)), trimmer)
            .addStudAt(totalWidth.subtract(this.studDimension.width), king)
        val crippleStudHeight = totalHeight.subtract(topOfHeader)
        if (crippleStudHeight != Measurement(0)) {
            crippleStud = Stud(crippleStudHeight, studDimension)
            crippleLayout = CrippleLayout(header.totalWidth(), crippleStud)
        } else {
            crippleStud = null
            crippleLayout = null
        }
    }

    /**
     *
     * @return - A copy of the [Measurement] of the height of this opening
     */
    override fun totalHeight(): Measurement {
        return totalHeight
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this opening
     */
    override fun totalWidth(): Measurement {
        return totalWidth
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this opening
     */
    override fun materialList(): MaterialList {
        val result = MaterialList()
            .addMaterials(floorLayout.materialList())
            .addMaterials(header.materialList())
            .addMaterial(Stud.nailType, extraNails)
        crippleLayout?.let { result.addMaterials(it.materialList()) }
        return result
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this opening
     */
    override fun graphicsList(): GraphicsList {
        val result = GraphicsList()
            .addGraphics(floorLayout.graphicsList())
            .addGraphics(header.graphicsList().shift(studDimension.width, totalHeight.subtract(topOfHeader)))
        crippleLayout?.let { result.addGraphics(it.graphicsList().shift(studDimension.width, Measurement(0))) }
        return result
    }

    /**
     * This method is intending to take a stud that was in a wall layout and move it into the opening's layout instead.
     * If the stud happens to conflict with a King stud of the opening, it will be moved next to it.
     * @param stud - A stud replaced by this opening
     * @param locationInOpening - The location of the stud relative to the start of the opening (the king stud)
     * @return This opening
     */
    @Throws(InstallableLocationConflict::class)
    open fun addCrippleStud(locationInOpening: Measurement): Opening {
        if (locationInOpening > this.totalWidth) {
            val error = "Stud cannot be added at $locationInOpening. Opening is only ${this.totalWidth} wide"
            throw InstallableLocationConflict(error, locationInOpening)
        }

        // If the cripple stud need to go before the first or after the last, it can be ignored as there is a king stud
        // there already
        val firstCrippleStud = this.studDimension.width
        val lastCrippleStud = totalWidth().subtract(this.studDimension.width.multiply(2))
        if (locationInOpening < firstCrippleStud || locationInOpening >= lastCrippleStud) {
            return this
        }

        // The cripple stud layout does not align with the outside of the Opening, so we need to shift the studs
        crippleLayout?.addStudAt(locationInOpening.subtract(this.studDimension.width), crippleStud!!)
        return this
    }

    /**
     * openings are only equal if the type of opening and the tripe layout are the same
     * @param other - the object to be compared
     * @return Indication if the two openings are equal
     */
    override fun equals(other: Any?) = (other === this)
            || (other is Opening && width == other.width && crippleLayout == other.crippleLayout)

    /**
     *
     * @return hash code based on the type and tripleLayout
     */
    override fun hashCode() = width.hashCode() + crippleLayout.hashCode()
}
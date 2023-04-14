package models.buildable.installable

import graphics.GraphicsList
import models.buildable.installable.Layout.InstallableLocationConflict
import models.buildable.material.Lumber
import models.buildable.material.MaterialList
import models.Measurement

/**
 * A class designed to store information about an opening (doors, windows ect)
 * @param width - Width of the opening to create
 * @param height - Height of the opening to create
 * @param kingStudHeight - Height of the kingStuds to use
 * @param heightToBottomOfOpening - The height to the bottom of the desired opening
 */
open class Opening(
    width: Measurement,
    height: Measurement,
    kingStudHeight: Measurement,
    // Defaults to 0 for Door type openings
    heightToBottomOfOpening: Measurement = Measurement(0),
) :
    Installable {
    private val maximumGap = Measurement(85)
    private val studDimension = Lumber.Dimension.TWO_BY_FOUR

    init {
        val error = "must be greater than ${Measurement(0)}"
        require(width > Measurement(0)) { "Width $error" }
        require(height > Measurement(0)) { "Height $error" }
        require((heightToBottomOfOpening >= studDimension.width) || (heightToBottomOfOpening == Measurement(0))) {
            "Height to bottom of opening must be greater than ${studDimension.width} or ${Measurement(0)}"
        }
    }

    private val kingStud: Stud
    private val trimmerStud: Stud
    private val header: Header
    private val kingLayout: Layout = Layout()
    private val trimmerLayout: CrippleLayout
    private val staticMaterials: MaterialList = MaterialList()
    private val staticGraphics: GraphicsList = GraphicsList()

    private val headerCrippleStud: Stud?
    private val headerCrippleLayout: CrippleLayout?

    private val floorCrippleLayoutDownShift: Measurement?
    private val floorCrippleStud: Stud?
    private val floorCrippleLayout: CrippleLayout?

    init {
        val loadBearingDimension = calculateLoadBearingDimension(width)
        val trimmerHeight = height.add(heightToBottomOfOpening)
        trimmerStud = if (loadBearingDimension == Lumber.Dimension.TWO_BY_SIX) {
            Stud(trimmerHeight, studDimension)
        } else {
            DoubleStud(trimmerHeight, studDimension)
        }

        val headerWidth = width.add(trimmerStud.totalWidth().multiply(2))
        header = Header(Stud(headerWidth, loadBearingDimension), Plate(headerWidth, studDimension))

        val minimumHeight = trimmerStud.totalHeight().add(header.totalHeight())

        require(kingStudHeight >= minimumHeight) {
            "Total height of $kingStudHeight is too short, requires $minimumHeight"
        }

        kingStud = Stud(kingStudHeight, this.studDimension)

        // The king and trimmer layouts are separated because Trimmers need to be attached to kings similar to how
        // cripple studs are attached to the outside studs.
        val lastKingStudLocation = kingStud.totalWidth().add(header.totalWidth())
        kingLayout
            .addStudAt(Measurement(0), kingStud)
            .addStudAt(lastKingStudLocation, kingStud)

        trimmerLayout = CrippleLayout(
            kingStud.totalWidth(),
            lastKingStudLocation.subtract(trimmerStud.totalWidth()),
            trimmerStud
        )

        /*
        static materials and graphics need to be calculated before cripple studs since those layouts can change
        as cripple studs are added
         */
        staticMaterials
            .addMaterials(kingLayout.materialList())
            .addMaterials(trimmerLayout.materialList())
            .addMaterials(header.materialList())

        val headerCrippleStudHeight = kingStudHeight.subtract(minimumHeight)
        staticGraphics
            .addGraphics(kingLayout.graphicsList())
            .addGraphics(
                trimmerLayout.graphicsList().shift(Measurement(0), headerCrippleStudHeight.add(header.totalHeight()))
            )
            .addGraphics(header.graphicsList().shift(kingStud.totalWidth(), headerCrippleStudHeight))

        if (headerCrippleStudHeight != Measurement(0)) {
            val headerCrippleLayoutShift = kingStud.totalWidth()
            headerCrippleStud = Stud(headerCrippleStudHeight, studDimension)
            headerCrippleLayout = CrippleLayout(
                firstStudPosition = headerCrippleLayoutShift,
                lastStudPosition = lastKingStudLocation.subtract(headerCrippleLayoutShift),
                studType = headerCrippleStud
            )
        } else {
            headerCrippleStud = null
            headerCrippleLayout = null
        }

        if (heightToBottomOfOpening > Measurement(0)) {
            val floorCripplePlate = Plate(width, studDimension)
            val floorCrippleLayoutInsideShift = kingStud.totalWidth().add(trimmerStud.totalWidth())
            floorCrippleLayoutDownShift =
                kingStudHeight.subtract(heightToBottomOfOpening).add(floorCripplePlate.totalHeight())

            staticMaterials.addMaterials(floorCripplePlate.materialList())
            staticGraphics.addGraphics(
                floorCripplePlate
                    .graphicsList()
                    .shift(floorCrippleLayoutInsideShift, kingStudHeight.subtract(heightToBottomOfOpening))
            )

            floorCrippleStud = Stud(heightToBottomOfOpening.subtract(floorCripplePlate.totalHeight()), studDimension)
            floorCrippleLayout = CrippleLayout(
                firstStudPosition = floorCrippleLayoutInsideShift,
                lastStudPosition = lastKingStudLocation.subtract(floorCrippleLayoutInsideShift),
                studType = floorCrippleStud
            )
        } else {
            floorCrippleLayoutDownShift = null
            floorCrippleStud = null
            floorCrippleLayout = null
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun calculateLoadBearingDimension(gapWidth: Measurement): Lumber.Dimension {
        return if (gapWidth <= Measurement(48)) {
            Lumber.Dimension.TWO_BY_SIX
        } else if (gapWidth <= Measurement(60)) {
            Lumber.Dimension.TWO_BY_EIGHT
        } else if (gapWidth <= Measurement(74)) {
            Lumber.Dimension.TWO_BY_TEN
        } else if (gapWidth <= maximumGap) {
            Lumber.Dimension.TWO_BY_TWELVE
        } else {
            throw IllegalArgumentException("Opening can only be $maximumGap wide")
        }
    }

    /**
     *
     * @return - A copy of the [Measurement] of the height of this opening
     */
    override fun totalHeight(): Measurement {
        return kingLayout.totalHeight()
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this opening
     */
    override fun totalWidth(): Measurement {
        return kingLayout.totalWidth()
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this opening
     */
    override fun materialList(): MaterialList {
        val result = MaterialList().addMaterials(staticMaterials)
        headerCrippleLayout?.let { result.addMaterials(it.materialList()) }
        floorCrippleLayout?.let { result.addMaterials(it.materialList()) }
        return result
    }

    /**
     *
     * @return - A [GraphicsList] of [GraphicsInstructions] used to draw this opening
     */
    override fun graphicsList(): GraphicsList {
        val result = GraphicsList().addGraphics(staticGraphics)
        headerCrippleLayout?.let { result.addGraphics(it.graphicsList()) }
        floorCrippleLayout?.let {
            result.addGraphics(
                it.graphicsList().shift(Measurement(0), floorCrippleLayoutDownShift!!)
            )
        }
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
        if (locationInOpening > this.totalWidth()) {
            val error = "Stud cannot be added at $locationInOpening. Opening is only ${this.totalWidth()} wide"
            throw InstallableLocationConflict(error, locationInOpening)
        }
        headerCrippleLayout?.addStudAt(locationInOpening, headerCrippleStud!!)
        floorCrippleLayout?.addStudAt(locationInOpening, floorCrippleStud!!)
        return this
    }

}
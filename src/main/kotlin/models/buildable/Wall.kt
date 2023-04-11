package models.buildable

import graphics.GraphicsList
import models.buildable.installable.*
import models.buildable.material.*
import models.Measurement
import models.Measurement.Fraction
import java.util.*
import kotlin.IllegalArgumentException

/**
 * Class used to describe what it takes to build a wall
 * @param length - Length of wall to create
 * @param height - Height of wall to create
 * @throws IllegalArgumentException - Thrown when the length or height is less than the minimum wall length/height
 */
class Wall internal constructor(
    private val length: Measurement,
    height: Measurement = Measurement(97, Fraction.ONE_EIGHTH)
) : Installable {
    private val platesHeightMap: TreeMap<Measurement, Plate> = TreeMap()

    // Plate Materials is not exactly the same as the combination of 2 or 3 plates because of how Plates calculate nails
    private val plateMaterials = MaterialList()
    private val studHeightShift: Measurement

    init {
        // All walls have at least one top plate, but the nails to attach it are from the studs
        val genericTopPlate: Plate
        try {
            genericTopPlate = Plate(length, studType)
        } catch (e: Stud.InvalidLengthException) {
            throw IllegalArgumentException("Wall cannot be longer than ${e.maxLength}")
        }
        platesHeightMap[Measurement(0)] = genericTopPlate
        this.plateMaterials.addMaterial(Lumber(length, studType), 1)

        // Add the bottom plate
        platesHeightMap[height.subtract(studType.width)] = Plate(length, studType)
        plateMaterials.addMaterials(genericTopPlate.materialList())

        // if loadBearing, there are two top plates
        if (loadBearing) {
            platesHeightMap[studType.width] = genericTopPlate
            plateMaterials.addMaterials(genericTopPlate.materialList())
            studHeightShift = studType.width.multiply(2)
        } else {
            studHeightShift = studType.width
        }
    }

    private val studHeight: Measurement
    private val stud: Stud
    private val layout: Layout

    init {
        // The height of a wall includes the top and bottom plates and the studs, so we need to remove the height of the
        // plates to get the heights of the studs
        studHeight =
            validateParameterMinimumMeasurement(height, minimumHeight, "Height").subtract(minimumHeight)
        try {
            stud = Stud(studHeight, studType)
        } catch (e: Stud.InvalidLengthException) {
            throw IllegalArgumentException("Wall cannot be taller than ${e.maxLength.add(studHeightShift)}")
        }
        layout = createLayout(
            validateParameterMinimumMeasurement(
                length, minimumWidth,
                "Length"
            )
        )
    }

    private fun createLayout(length: Measurement): Layout {
        val currentLayout = Layout()
        var currentPosition: Measurement = Measurement(0)

        // add the initial stud
        currentLayout.addStudAt(currentPosition, stud)
        currentPosition = currentPosition.add(studSeparation)

        // add the rest of the studs. There needs to be enough room for both the full separation and the width of
        // two studs, the stud to add and the final stud
        val furthestAdditionalStud = length.subtract(studType.width.multiply(2))
        while (currentPosition < furthestAdditionalStud) {
            currentLayout.addStudAt(currentPosition, stud)
            currentPosition = currentPosition.add(studSeparation)
        }

        // It possible for the difference between the last added stud and the final stud to be greater than the
        // studSeparation. In this case, we need a stud to be right next to the final stud in the location of
        // furthestAdditionalStud.
        val finalStudPosition = length.subtract(studType.width)

        // Since Measurements have to be positive, the currentPosition needs to be backed up by a studSeparation
        // to ensure finalStudPosition.subtract(currentPosition) won't ever be negative. Then the difference can
        // be compared to the studSeparation to see which is bigger
        if (finalStudPosition.subtract(currentPosition.subtract(studSeparation)) > studSeparation) {

            // A double stud here will ensure the correct number of nails are added
            currentLayout.addStudAt(furthestAdditionalStud, DoubleStud(stud))
        } else {
            // add the final stud, so it touches the end of the wall
            currentLayout.addStudAt(finalStudPosition, stud)
        }
        return currentLayout
    }

    /**
     *
     * @return The studs required to create this wall
     */
    fun layout(): Layout {
        return layout
    }

    @Throws(IllegalArgumentException::class)
    fun addADoor(ofType: StandardDoor, atLocation: Measurement): Wall {
        val attemptedDoor = Opening(ofType.openingWidth, ofType.openingHeight, studHeight)
        validateAddedOpeningLocation("$ofType door", atLocation, attemptedDoor)
        layout.addOpeningAt(attemptedDoor, atLocation)
        return this
    }

    @Throws(IllegalArgumentException::class)
    fun addAWindow(
        atLocation: Measurement,
        bottomOfWindowHeight: Measurement,
        windowWidth: Measurement,
        windowHeight: Measurement,
    ): Wall {
        val attemptedWindow = Window(windowWidth, windowHeight, bottomOfWindowHeight, studHeight)
        validateAddedOpeningLocation("Window", atLocation, attemptedWindow)
        layout.addOpeningAt(attemptedWindow, atLocation)
        return this
    }

    @Throws(IllegalArgumentException::class)
    private fun validateAddedOpeningLocation(additionType: String, atLocation: Measurement, addition: Opening) {
        val baseErrorMessage = "$additionType cannot be installed at $atLocation,"
        require(atLocation.add(addition.totalWidth()) <= this.totalWidth()) {
            "$baseErrorMessage $additionType is ${addition.totalWidth()} wide and wall is ${this.totalWidth()} long"
        }
    }

    /**
     *
     * @return - A copy of the [Measurement] of the width of this Wall
     */
    override fun totalWidth(): Measurement {
        return layout.totalWidth()
    }

    /**
     *
     * @return - A copy of the [Measurement] of the height of this Wall
     */
    override fun totalHeight(): Measurement {
        return layout.totalHeight()
    }

    /**
     *
     * @return - A [MaterialList] of [models.buildable.material.Material] used to create this Wall
     */
    override fun materialList(): MaterialList {
        return MaterialList().addMaterials(plateMaterials).addMaterials(layout.materialList())
    }

    /**
     *
     * @return - A [GraphicsList] of [graphics.GraphicsInstructions] used to draw this Wall
     */
    override fun graphicsList(): GraphicsList {
        val result = GraphicsList()
        val zero = Measurement(0)

        // Plates need to be shifted according to where it is located in the wall
        platesHeightMap.forEach { (measurement: Measurement, plate: Plate) ->
            result.addGraphics(plate.graphicsList().shift(zero, measurement))
        }
        result.addGraphics(layout.graphicsList().shift(zero, studHeightShift))
        return result
    }

    companion object {
        private const val loadBearing = true
        private val studType = Lumber.Dimension.TWO_BY_FOUR
        private val studSeparation = Measurement(16)
        private const val minimumNumberOfStuds = 2
        val minimumHeight = studType.width.multiply(3)
        val minimumWidth = studType.width.multiply(minimumNumberOfStuds)

        @Throws(IllegalArgumentException::class)
        private fun validateParameterMinimumMeasurement(
            parameter: Measurement,
            minimumValue: Measurement,
            type: String
        ): Measurement {
            if (parameter < minimumValue) {
                throw IllegalArgumentException("$type cannot be less than $minimumValue, was $parameter")
            }
            return parameter
        }
    }
}
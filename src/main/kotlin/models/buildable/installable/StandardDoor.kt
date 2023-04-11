package models.buildable.installable

import models.Measurement

/**
 * OpeningWidth and OpeningHeights of doors are inclusive of the extra 'wiggle' room for doors when installed
 */
enum class StandardDoor(width: Measurement, height: Measurement) {
    Bedroom(Measurement(32), Measurement(80));

    val openingWidth = Measurement(2).add(width)
    val openingHeight = Measurement(1).add(height)
}
package models.buildable.installable

import models.buildable.installable.Door.StandardDoor
import models.buildable.material.Lumber
import models.Measurement

class KingStud(length: Measurement, dimension: Lumber.Dimension, doorType: StandardDoor) : DoubleStud(length, dimension)
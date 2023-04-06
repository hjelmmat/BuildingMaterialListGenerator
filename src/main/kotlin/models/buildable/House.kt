package models.buildable

import graphics.GraphicsList
import models.Measurement
import models.buildable.material.MaterialList
import java.util.*

class House : Buildable {
    private var wall: Wall? = null

    @Throws(IllegalArgumentException::class)
    fun addWall(wallLength: Measurement, wallHeight: Measurement): House {
        wall = Wall(wallLength, wallHeight)
        return this
    }

    @Throws(NullPointerException::class)
    override fun materials(): Vector<Vector<String>> {
        return wall?.materials() ?: MaterialList().materials()
    }
}
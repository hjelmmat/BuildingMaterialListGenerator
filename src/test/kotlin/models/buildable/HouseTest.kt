package models.buildable

import models.Measurement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class HouseTest {
    @Test
    fun shouldAddWall() {
        val results = Vector<Vector<String>>()
        results.add(Vector(listOf("24\" 2x4", "3")))
        results.add(Vector(listOf("48\" 2x4", "3")))
        results.add(Vector(listOf("10d nails", "30")))
        Assertions.assertEquals(results, House().addWall(Measurement(24), Measurement(30)).materials())
    }
}
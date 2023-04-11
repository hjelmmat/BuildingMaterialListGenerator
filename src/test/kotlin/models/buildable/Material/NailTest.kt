package models.buildable.material

import kotlin.test.*

internal class NailTest {
    @Test
    fun shouldReturnNiceString() {
        val result = "10d nails"
        assertEquals(result, Nail.TEN_D.toString())
    }
}
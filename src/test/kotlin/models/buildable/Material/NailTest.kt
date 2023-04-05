package models.buildable.material

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class NailTest {
    @Test
    fun shouldReturnNiceString() {
        val result = "10d nails"
        Assertions.assertEquals(result, Nail.TEN_D.toString())
    }
}
package ui.models.materials

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ui.models.materials.Material

class MaterialTest {
    @Test
    fun shouldCalculateEquals() {
        val test = Material("a", "b")
        val result = Material("a", "c")
        assertNotEquals(result, test)
        val secondTest = Material("a", "c")
        assertEquals(result, secondTest)
        val thirdTest = Material("b", "c")
        assertNotEquals(result, thirdTest)
    }
}
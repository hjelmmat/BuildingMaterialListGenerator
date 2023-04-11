package models.buildable.material

import models.Measurement
import models.Measurement.Fraction
import kotlin.test.*

internal class LumberTest {
    @Test
    fun lumberShouldThrowWhenLengthIsTooLong() {
        val thrown = assertFailsWith<IllegalArgumentException> {
            Lumber(Measurement(240, Fraction.ONE_SIXTEENTH), Lumber.Dimension.TWO_BY_FOUR)
        }
        val message = "A length cannot be greater than 240\", was 240-1/16\""
        assertEquals(message, thrown.message)
    }

    @Test
    fun lumberShouldCompareEqual() {
        val lumber1 = Lumber(Measurement(3), Lumber.Dimension.TWO_BY_FOUR)
        val lumber2 = Lumber(Measurement(3), Lumber.Dimension.TWO_BY_FOUR)
        assertTrue { lumber1 == lumber2 }
        val lumber3 = Lumber(Measurement(10), Lumber.Dimension.TWO_BY_FOUR)
        assertTrue { lumber1 == lumber3 }
        val lumber4 = Lumber(Measurement(50), Lumber.Dimension.TWO_BY_FOUR)
        assertFalse { lumber1 == lumber4 }
    }

    @Test
    fun lumberShouldProduceUniqueHash() {
        val result = 2142734874
        assertEquals(result, Lumber(Measurement(3), Lumber.Dimension.TWO_BY_FOUR).hashCode())
        val secondResult = -1041948622
        assertEquals(
            secondResult,
            Lumber(Measurement(92, Fraction.FIVE_EIGHTH),Lumber.Dimension.TWO_BY_FOUR,).hashCode(),
        )
    }

    @Test
    fun lumberShouldProduceNiceString() {
        val fourFootResult = "48\" 2x4"
        assertEquals(fourFootResult, Lumber(Measurement(40), Lumber.Dimension.TWO_BY_FOUR).toString())
    }
}
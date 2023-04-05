package models.buildable.material

import models.Measurement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class MaterialListTest {
    private var result: Vector<Vector<Any>> = Vector()
    private val lumber = Lumber(Measurement(48), Lumber.Dimension.TWO_BY_FOUR)

    @BeforeEach
    fun setup() {
        result.clear()
        result.add(Vector(listOf(lumber.toString(), "2")))
    }

    @Test
    fun materialListShouldIncreaseQuantityWithNewMaterial() {
        Assertions.assertEquals(result, MaterialList().addMaterial(lumber, 1).addMaterial(lumber, 1).materials())
    }

    @Test
    fun shouldReturnContentsVector() {
        val test = MaterialList().addMaterial(lumber, 2)
        Assertions.assertEquals(result, test.materials())
        result.add(Vector(listOf("10d nails", "2")))
        Assertions.assertEquals(result, test.addMaterial(Nail.TEN_D, 2).materials())
    }

    @Test
    fun shouldReturnEqual() {
        val one = MaterialList().addMaterial(lumber, 1)
        val two = MaterialList().addMaterial(lumber, 1)
        Assertions.assertEquals(one, two)
    }

    @Test
    fun shouldCreateHashCode() {
        val result = 1105730073
        Assertions.assertEquals(result, MaterialList().addMaterial(lumber, 1).hashCode())
    }
}
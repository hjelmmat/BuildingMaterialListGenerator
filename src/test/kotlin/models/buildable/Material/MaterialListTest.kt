package models.buildable.material

import models.Measurement
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.HashMap
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class MaterialListTest {
    private var result: HashMap<String, Vector<Vector<String>>> = HashMap()
    private val lumber = Lumber(Measurement(48), Lumber.Dimension.TWO_BY_FOUR)

    @BeforeTest
    fun setup() {
        result.clear()
        val emptyLumber = Vector<Vector<String>>()
        emptyLumber.add(Vector(listOf(lumber.toString(), "2")))
        result["lumber"] = emptyLumber
    }

    @Test
    fun materialListShouldIncreaseQuantityWithNewMaterial() {
        assertEquals(result, MaterialList().addMaterial(lumber, 1).addMaterial(lumber, 1).materials())
    }

    @Test
    fun shouldReturnContentsVector() {
        val test = MaterialList().addMaterial(lumber, 2)
        assertEquals(result, test.materials())
        val emptyNails = Vector<Vector<String>>()
        emptyNails.add(Vector(listOf("10d nails", "2")))
        result["nails"] = emptyNails
        assertEquals(result, test.addMaterial(Nail.TEN_D, 2).materials())
    }

    @Test
    fun shouldReturnEqual() {
        val one = MaterialList().addMaterial(lumber, 1)
        val two = MaterialList().addMaterial(lumber, 1)
        assertEquals(one, two)
    }

    @Test
    fun shouldCreateHashCode() {
        val result = 1105730073
        assertEquals(result, MaterialList().addMaterial(lumber, 1).hashCode())
    }
}
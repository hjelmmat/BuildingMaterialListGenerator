package models

import models.Measurement.Fraction
import models.Measurement.InvalidMeasurementException
import kotlin.test.*

internal class MeasurementTest {
    private var errorMessage = "measurement cannot be less than 0, was -1"
    private var measurementOfFour = Measurement(4)
    private var secondMeasurementOfFour = Measurement(4)
    private var eight = Measurement(8)

    @Test
    fun measurementShouldThrowWhenParameterIsInvalid() {
        val negativeValue = -1
        val thrown = assertFailsWith<InvalidMeasurementException> { Measurement(negativeValue) }
        assertEquals(errorMessage, thrown.message)
        val fractionValue = Fraction.ZERO
        val secondThrow = assertFailsWith<InvalidMeasurementException> {
            Measurement(
                negativeValue,
                fractionValue
            )
        }
        assertEquals(errorMessage, secondThrow.message)
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldBeComparable() {
        assertNotSame(measurementOfFour, secondMeasurementOfFour)
        assertEquals(0, measurementOfFour.compareTo(secondMeasurementOfFour))
        val measurementOfFive = Measurement(5)
        assertEquals(-1, measurementOfFour.compareTo(measurementOfFive))
        assertEquals(1, measurementOfFive.compareTo(measurementOfFour))
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        assertEquals(-1, measurementOfFour.compareTo(measurementOfFourAndOneHalf))
        assertEquals(1, measurementOfFourAndOneHalf.compareTo(measurementOfFour))
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldCompareEquality() {
        assertEquals(measurementOfFour, secondMeasurementOfFour)
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        assertNotEquals(measurementOfFour, measurementOfFourAndOneHalf)
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldCreateUniqueHash() {
        val onlyFractionExpectedValue = 625
        assertEquals(onlyFractionExpectedValue, Measurement(0, Fraction.ONE_SIXTEENTH).hashCode())
        val intAndFractionExpectedValue = 54375
        assertEquals(intAndFractionExpectedValue, Measurement(5, Fraction.SEVEN_SIXTEENTH).hashCode())
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldMultiplyByInt() {
        val simpleMultiplicand = 1
        val measurementOfFive = Measurement(5)
        assertEquals(measurementOfFive, measurementOfFive.multiply(simpleMultiplicand))
        val doubleMultiplicand = 2
        val four = Measurement(4)
        assertEquals(eight, four.multiply(doubleMultiplicand))
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        assertEquals(Measurement(9), measurementOfFourAndOneHalf.multiply(doubleMultiplicand))
        assertEquals(
            Measurement(1, Fraction.ONE_EIGHTH),
            Measurement(0, Fraction.NINE_SIXTEENTH).multiply(doubleMultiplicand),
        )
        val largeMultiplicand = 25
        assertEquals(
            Measurement(51, Fraction.NINE_SIXTEENTH),
            Measurement(2, Fraction.ONE_SIXTEENTH).multiply(largeMultiplicand),
        )
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldDivideByMeasurement() {
        assertEquals(1.0, measurementOfFour.divide(measurementOfFour))
        assertEquals(2.0, eight.divide(measurementOfFour))
        assertEquals(.5, measurementOfFour.divide(eight))
    }

    @Test
    fun measurementShouldSubtractAMeasurement() {
        val zeroMeasure = Measurement(0, Fraction.ZERO)
        val measurementOfFive = Measurement(5)
        val secondMeasurementOfFive = Measurement(5)
        assertEquals(zeroMeasure, measurementOfFive.subtract(secondMeasurementOfFive))
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        val newMeasurementOfFour = Measurement(4)
        assertEquals(
            Measurement(0, Fraction.ONE_HALF),
            measurementOfFourAndOneHalf.subtract(newMeasurementOfFour),
        )
        val fiveAndOneHalf = Measurement(5, Fraction.ONE_HALF)
        assertEquals(
            fiveAndOneHalf,
            Measurement(13, Fraction.FIVE_SIXTEENTH).subtract(Measurement(7, Fraction.THIRTEEN_SIXTEENTH)),
        )
        val oneHalf = Measurement(0, Fraction.ONE_HALF)
        val thrown = assertFailsWith<InvalidMeasurementException> { zeroMeasure.subtract(oneHalf) }
        val error = "measurement cannot be less than 0, was -1/2\""
        assertEquals(error, thrown.message)
    }

    @Test
    fun measurementShouldAddAMeasurement() {
        val measurementOfFive = Measurement(5)
        assertEquals(eight, measurementOfFive.add(Measurement(3)))
    }

    @Test
    fun measurementShouldRepresentAsAString() {
        /*
        Since Enums can't get unit tested, this is also testing the logic to create Fraction.niceString
         */
        val zeroResult = "0\""
        val zeroMeasure = Measurement(0, Fraction.ZERO)
        assertEquals(zeroResult, zeroMeasure.toString())
        val zeroFraction = "5\""
        assertEquals(zeroFraction, Measurement(5, Fraction.ZERO).toString())
        val threeEighths = "5/8\""
        assertEquals(threeEighths, Measurement(0, Fraction.FIVE_EIGHTH).toString())
        val fourAndOneHalf = "4-1/2\""
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        assertEquals(fourAndOneHalf, measurementOfFourAndOneHalf.toString())
        val tenAndNineSixteenths = "10-9/16\""
        assertEquals(tenAndNineSixteenths, Measurement(10, Fraction.NINE_SIXTEENTH).toString())
        val eighteenAndThreeFourths = "18-3/4\""
        assertEquals(eighteenAndThreeFourths, Measurement(18, Fraction.THREE_FOURTH).toString())
    }

    @Test
    fun measurementShouldCalculateNumberOfPixels() {
        val result = 8
        assertEquals(result, Measurement(1).numberOfPixels)
        val secondResult = 12
        assertEquals(secondResult, Measurement(1, Fraction.ONE_HALF).numberOfPixels)
    }
}
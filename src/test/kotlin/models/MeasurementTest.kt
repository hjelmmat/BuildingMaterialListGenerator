package models

import models.Measurement.Fraction
import models.Measurement.InvalidMeasurementException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MeasurementTest {
    private var errorMessage = "measurement cannot be less than 0, was -1"
    private var measurementOfFour = Measurement(4)
    private var secondMeasurementOfFour = Measurement(4)
    private var eight = Measurement(8)

    @Test
    fun measurementShouldThrowWhenParameterIsInvalid() {
        val negativeValue = -1
        val thrown = Assertions.assertThrows(InvalidMeasurementException::class.java) { Measurement(negativeValue) }
        Assertions.assertEquals(errorMessage, thrown.message)
        val fractionValue = Fraction.ZERO
        val secondThrow = Assertions.assertThrows(InvalidMeasurementException::class.java) {
            Measurement(
                negativeValue,
                fractionValue
            )
        }
        Assertions.assertEquals(errorMessage, secondThrow.message)
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldBeComparable() {
        Assertions.assertNotSame(measurementOfFour, secondMeasurementOfFour)
        Assertions.assertEquals(0, measurementOfFour.compareTo(secondMeasurementOfFour))
        val measurementOfFive = Measurement(5)
        Assertions.assertEquals(-1, measurementOfFour.compareTo(measurementOfFive))
        Assertions.assertEquals(1, measurementOfFive.compareTo(measurementOfFour))
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        Assertions.assertEquals(-1, measurementOfFour.compareTo(measurementOfFourAndOneHalf))
        Assertions.assertEquals(1, measurementOfFourAndOneHalf.compareTo(measurementOfFour))
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldCompareEquality() {
        Assertions.assertEquals(measurementOfFour, secondMeasurementOfFour)
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        Assertions.assertNotEquals(measurementOfFour, measurementOfFourAndOneHalf)
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldCreateUniqueHash() {
        val onlyFractionExpectedValue = 625
        Assertions.assertEquals(onlyFractionExpectedValue, Measurement(0, Fraction.ONE_SIXTEENTH).hashCode())
        val intAndFractionExpectedValue = 54375
        Assertions.assertEquals(intAndFractionExpectedValue, Measurement(5, Fraction.SEVEN_SIXTEENTH).hashCode())
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldMultiplyByInt() {
        val simpleMultiplicand = 1
        val measurementOfFive = Measurement(5)
        Assertions.assertEquals(measurementOfFive, measurementOfFive.multiply(simpleMultiplicand))
        val doubleMultiplicand = 2
        val four = Measurement(4)
        Assertions.assertEquals(eight, four.multiply(doubleMultiplicand))
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        Assertions.assertEquals(Measurement(9), measurementOfFourAndOneHalf.multiply(doubleMultiplicand))
        Assertions.assertEquals(
            Measurement(1, Fraction.ONE_EIGHTH),
            Measurement(0, Fraction.NINE_SIXTEENTH).multiply(doubleMultiplicand),
        )
        val largeMultiplicand = 25
        Assertions.assertEquals(
            Measurement(51, Fraction.NINE_SIXTEENTH),
            Measurement(2, Fraction.ONE_SIXTEENTH).multiply(largeMultiplicand),
        )
    }

    @Test
    @Throws(InvalidMeasurementException::class)
    fun measurementShouldDivideByMeasurement() {
        Assertions.assertEquals(1.0, measurementOfFour.divide(measurementOfFour))
        Assertions.assertEquals(2.0, eight.divide(measurementOfFour))
        Assertions.assertEquals(.5, measurementOfFour.divide(eight))
    }

    @Test
    fun measurementShouldSubtractAMeasurement() {
        val zeroMeasure = Measurement(0, Fraction.ZERO)
        val measurementOfFive = Measurement(5)
        val secondMeasurementOfFive = Measurement(5)
        Assertions.assertEquals(zeroMeasure, measurementOfFive.subtract(secondMeasurementOfFive))
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        val newMeasurementOfFour = Measurement(4)
        Assertions.assertEquals(
            Measurement(0, Fraction.ONE_HALF),
            measurementOfFourAndOneHalf.subtract(newMeasurementOfFour),
        )
        val fiveAndOneHalf = Measurement(5, Fraction.ONE_HALF)
        Assertions.assertEquals(
            fiveAndOneHalf,
            Measurement(13, Fraction.FIVE_SIXTEENTH).subtract(Measurement(7, Fraction.THIRTEEN_SIXTEENTH)),
        )
        val oneHalf = Measurement(0, Fraction.ONE_HALF)
        val thrown = Assertions.assertThrows(InvalidMeasurementException::class.java) { zeroMeasure.subtract(oneHalf) }
        val error = "measurement cannot be less than 0, was -1/2\""
        Assertions.assertEquals(error, thrown.message)
    }

    @Test
    fun measurementShouldAddAMeasurement() {
        val measurementOfFive = Measurement(5)
        Assertions.assertEquals(eight, measurementOfFive.add(Measurement(3)))
    }

    @Test
    fun measurementShouldRepresentAsAString() {
        /*
        Since Enums can't get unit tested, this is also testing the logic to create Fraction.niceString
         */
        val zeroResult = "0\""
        val zeroMeasure = Measurement(0, Fraction.ZERO)
        Assertions.assertEquals(zeroResult, zeroMeasure.toString())
        val zeroFraction = "5\""
        Assertions.assertEquals(zeroFraction, Measurement(5, Fraction.ZERO).toString())
        val threeEighths = "5/8\""
        Assertions.assertEquals(threeEighths, Measurement(0, Fraction.FIVE_EIGHTH).toString())
        val fourAndOneHalf = "4-1/2\""
        val measurementOfFourAndOneHalf = Measurement(4, Fraction.ONE_HALF)
        Assertions.assertEquals(fourAndOneHalf, measurementOfFourAndOneHalf.toString())
        val tenAndNineSixteenths = "10-9/16\""
        Assertions.assertEquals(tenAndNineSixteenths, Measurement(10, Fraction.NINE_SIXTEENTH).toString())
        val eighteenAndThreeFourths = "18-3/4\""
        Assertions.assertEquals(eighteenAndThreeFourths, Measurement(18, Fraction.THREE_FOURTH).toString())
    }

    @Test
    fun measurementShouldCalculateNumberOfPixels() {
        val result = 8
        Assertions.assertEquals(result, Measurement(1).numberOfPixels)
        val secondResult = 12
        Assertions.assertEquals(secondResult, Measurement(1, Fraction.ONE_HALF).numberOfPixels)
    }
}
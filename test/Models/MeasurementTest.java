package Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementTest {
    String errorMessage = "measurement cannot be less than 0, was -1";
    Measurement measurementOfFour = new Measurement(4);
    Measurement secondMeasurementOfFour = new Measurement(4);
    Measurement eight = new Measurement(8);

    @Test
    public void measurementShouldThrowWhenParameterIsInvalid() {
        int negativeValue = -1;
        Measurement.InvalidMeasurementException thrown = assertThrows(Measurement.InvalidMeasurementException.class, () -> new Measurement(negativeValue));
        assertEquals(errorMessage, thrown.getMessage());

        Measurement.Fraction fractionValue = Measurement.Fraction.ZERO;
        Measurement.InvalidMeasurementException secondThrow = assertThrows(Measurement.InvalidMeasurementException.class, () -> new Measurement(negativeValue, fractionValue));
        assertEquals(errorMessage, secondThrow.getMessage());
    }

    @Test
    public void measurementShouldBeComparable() throws Measurement.InvalidMeasurementException {
        assertNotSame(this.measurementOfFour, this.secondMeasurementOfFour);
        assertEquals(0, this.measurementOfFour.compareTo(this.secondMeasurementOfFour));

        Measurement measurementOfFive = new Measurement(5);
        assertEquals(-1, this.measurementOfFour.compareTo(measurementOfFive));
        assertEquals(1, measurementOfFive.compareTo(this.measurementOfFour));

        Measurement measurementOfFourAndOneHalf = new Measurement(4, Measurement.Fraction.ONE_HALF);
        assertEquals(-1, this.measurementOfFour.compareTo(measurementOfFourAndOneHalf));
        assertEquals(1, measurementOfFourAndOneHalf.compareTo(this.measurementOfFour));
    }

    @Test
    public void measurementShouldCompareEquality() throws Measurement.InvalidMeasurementException {
        assertEquals(this.measurementOfFour, this.secondMeasurementOfFour);
        Measurement measurementOfFourAndOneHalf = new Measurement(4, Measurement.Fraction.ONE_HALF);
        assertNotEquals(this.measurementOfFour, measurementOfFourAndOneHalf);
    }

    @Test
    public void measurementShouldCreateUniqueHash() throws Measurement.InvalidMeasurementException {
        int onlyFractionExpectedValue = 625;
        assertEquals(onlyFractionExpectedValue, new Measurement(0, Measurement.Fraction.ONE_SIXTEENTH).hashCode());

        int intAndFractionExpectedValue = 54375;
        assertEquals(intAndFractionExpectedValue, new Measurement(5, Measurement.Fraction.SEVEN_SIXTEENTH).hashCode());
    }

    @Test
    public void measurementShouldMultiplyByInt() throws Measurement.InvalidMeasurementException {
        int simpleMultiplicand = 1;
        Measurement measurementOfFive = new Measurement(5);
        assertEquals(measurementOfFive, measurementOfFive.multiply(simpleMultiplicand));

        int doubleMultiplicand = 2;
        Measurement four = new Measurement(4);
        assertEquals(this.eight, four.multiply(doubleMultiplicand));
        Measurement measurementOfFourAndOneHalf = new Measurement(4, Measurement.Fraction.ONE_HALF);
        assertEquals(new Measurement(9), measurementOfFourAndOneHalf.multiply(doubleMultiplicand));
        assertEquals(new Measurement(1, Measurement.Fraction.ONE_EIGHTH),
                new Measurement(0, Measurement.Fraction.NINE_SIXTEENTH).multiply(doubleMultiplicand));

        int largeMultiplicand = 25;
        assertEquals(new Measurement(51, Measurement.Fraction.NINE_SIXTEENTH),
                new Measurement(2, Measurement.Fraction.ONE_SIXTEENTH).multiply(largeMultiplicand));
    }

    @Test
    public void measurementShouldDivideByMeasurement() throws Measurement.InvalidMeasurementException {
        assertEquals(1, this.measurementOfFour.divide(this.measurementOfFour));
        assertEquals(2, this.eight.divide(measurementOfFour));
        assertEquals(.5, this.measurementOfFour.divide(this.eight));
    }

    @Test
    public void measurementShouldSubtractAMeasurement() {
        Measurement zeroMeasure = new Measurement(0, Measurement.Fraction.ZERO);
        Measurement measurementOfFive = new Measurement(5);
        Measurement secondMeasurementOfFive = new Measurement(5);
        assertEquals(zeroMeasure, measurementOfFive.subtract(secondMeasurementOfFive));

        Measurement measurementOfFourAndOneHalf = new Measurement(4, Measurement.Fraction.ONE_HALF);
        Measurement newMeasurementOfFour = new Measurement(4);
        assertEquals(new Measurement(0, Measurement.Fraction.ONE_HALF),
                measurementOfFourAndOneHalf.subtract(newMeasurementOfFour));

        Measurement fiveAndOneHalf = new Measurement(5, Measurement.Fraction.ONE_HALF);
        assertEquals(fiveAndOneHalf,
                new Measurement(13, Measurement.Fraction.FIVE_SIXTEENTH).subtract(new Measurement(7, Measurement.Fraction.THIRTEEN_SIXTEENTH)));

        Measurement oneHalf = new Measurement(0, Measurement.Fraction.ONE_HALF);
        Measurement.InvalidMeasurementException thrown = assertThrows(Measurement.InvalidMeasurementException.class, () -> zeroMeasure.subtract(oneHalf));
        String error = "measurement cannot be less than 0, was -1/2\"";
        assertEquals(error, thrown.getMessage());
    }

    @Test
    public void measurementShouldAddAMeasurement() {
        Measurement measurementOfFive = new Measurement(5);
        assertEquals(this.eight, measurementOfFive.add(new Measurement(3)));
    }

    @Test
    public void measurementShouldRepresentAsAString() {
        /*
        Since Enums can't get unit tested, this is also testing the logic to create Fraction.niceString
         */
        String zeroResult = "0\"";
        Measurement zeroMeasure = new Measurement(0, Measurement.Fraction.ZERO);
        assertEquals(zeroResult, zeroMeasure.toString());

        String zeroFraction = "5\"";
        assertEquals(zeroFraction, new Measurement(5, Measurement.Fraction.ZERO).toString());

        String threeEighths = "5/8\"";
        assertEquals(threeEighths, new Measurement(0, Measurement.Fraction.FIVE_EIGHTH).toString());

        String fourAndOneHalf = "4-1/2\"";
        Measurement measurementOfFourAndOneHalf = new Measurement(4, Measurement.Fraction.ONE_HALF);
        assertEquals(fourAndOneHalf, measurementOfFourAndOneHalf.toString());

        String tenAndNineSixteenths = "10-9/16\"";
        assertEquals(tenAndNineSixteenths, new Measurement(10, Measurement.Fraction.NINE_SIXTEENTH).toString());

        String eighteenAndThreeFourths = "18-3/4\"";
        assertEquals(eighteenAndThreeFourths, new Measurement(18, Measurement.Fraction.THREE_FOURTH).toString());
    }

    @Test
    public void measurementShouldCalculateNumberOfPixels() {
        int result = 8;
        assertEquals(result, new Measurement(1).numberOfPixels());

        int secondResult = 12;
        assertEquals(secondResult, new Measurement(1, Measurement.Fraction.ONE_HALF).numberOfPixels());
    }

    @Test
    public void measurementShouldReturnGreaterMeasurement() {
        Measurement greater = new Measurement(10);
        Measurement lesser = new Measurement(5);
        assertEquals(greater, greater.greaterValue(lesser));
        assertEquals(greater, lesser.greaterValue(greater));
    }
}
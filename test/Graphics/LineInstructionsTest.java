package Graphics;

import Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class LineInstructionsTest {
    @Test
    public void shouldImplementDrawingInstructions() {
        Vector<Integer> result = new Vector<>(new Vector<>(List.of(new Measurement(1).numberOfPixels(),
                new Measurement(2).numberOfPixels(),
                new Measurement(3).numberOfPixels(),
                new Measurement(4).numberOfPixels())));
        assertEquals(result, new LineInstructions(new Measurement(1), new Measurement(2), new Measurement(3), new Measurement(4)).drawingInstructions());
    }

    @Test
    public void shouldShift() {
        Measurement zero = new Measurement(0);
        Measurement five = new Measurement(5);
        Measurement ten = new Measurement(10);
        Vector<Integer> result = new Vector<>(List.of(five.numberOfPixels(), ten.numberOfPixels(), five.numberOfPixels(), ten.numberOfPixels()));
        LineInstructions test = new LineInstructions(zero, zero, zero, zero);
        assertNotEquals(result, test.drawingInstructions());
        assertEquals(result, test.shift(five, ten).drawingInstructions());
        assertEquals(result, test.shift(five, ten).drawingInstructions());
    }
}
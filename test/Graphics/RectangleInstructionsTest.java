package Graphics;

import Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class RectangleInstructionsTest {
    Measurement zero = new Measurement(0);
    Measurement one = new Measurement(1);
    Measurement ten = new Measurement(10);
    @Test
    public void shouldImplementDrawingInstructions() {
        Integer one = new Measurement(1).numberOfPixels();
        Vector<Integer> result = new Vector<>(new Vector<>(List.of(one, one, one, one)));
        Measurement oneM = new Measurement(1);
        assertEquals(result, new RectangleInstructions(oneM, oneM, oneM, oneM).drawingInstructions());
    }

    @Test
    public void shouldShift() {
        Vector<Integer> result = new Vector<>(List.of(ten.numberOfPixels(), ten.numberOfPixels(), one.numberOfPixels(), one.numberOfPixels()));
        RectangleInstructions test = new RectangleInstructions(zero, zero, one, one);
        assertNotEquals(result, test.drawingInstructions());
        assertEquals(result, test.shift(ten, ten).drawingInstructions());
    }
}
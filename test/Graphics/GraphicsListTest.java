package Graphics;

import Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GraphicsListTest {
    Measurement zero = new Measurement(0);
    Measurement ten = new Measurement(10);
    @Test
    public void shouldAddLines() {
        Vector<Vector<Vector<Integer>>> result = new Vector<>();
        Vector<Vector<Integer>> lines = new Vector<>();
        Vector<Integer> line = new Vector<>(List.of(0,0,ten.numberOfPixels(), ten.numberOfPixels()));
        lines.add(line);
        lines.add(line);
        result.add(lines);
        result.add(new Vector<>());
        LineInstructions instruction = new LineInstructions(zero, zero, ten, ten);
        assertEquals(result, new GraphicsList().addGraphic(instruction).addGraphic(instruction).drawingInstructions());
    }

    @Test
    public void shouldAddRectangles() {
        Vector<Vector<Vector<Integer>>> result = new Vector<>();
        result.add(new Vector<>());
        Vector<Vector<Integer>> rectangles = new Vector<>();
        Vector<Integer> rectangle = new Vector<>(List.of(0,0,ten.numberOfPixels(), ten.numberOfPixels()));
        rectangles.add(rectangle);
        result.add(rectangles);
        assertEquals(result, new GraphicsList().addGraphic(new RectangleInstructions(zero, zero, ten, ten)).drawingInstructions());
    }

    @Test
    public void shouldAddGraphicsList() {
        GraphicsList result = new GraphicsList().addGraphic(new LineInstructions(zero, zero, zero, zero));
        assertEquals(result.drawingInstructions(), new GraphicsList().addGraphics(result).drawingInstructions());
    }

    @Test
    public void shouldShiftItems() {
        Vector<Vector<Vector<Integer>>> result = new Vector<>();
        Vector<Vector<Integer>> lines = new Vector<>();
        lines.add(new Vector<>(List.of(ten.numberOfPixels(), ten.numberOfPixels(), ten.numberOfPixels(), ten.numberOfPixels())));
        result.add(lines);
        result.add(new Vector<>());
        GraphicsList test = new GraphicsList().addGraphic(new LineInstructions(zero, zero, zero, zero));
        assertNotEquals(result, test.drawingInstructions());
        assertEquals(result, test.shift(ten, ten).drawingInstructions());
        assertEquals(result, test.shift(ten, ten).drawingInstructions());
    }
}
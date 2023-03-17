package Graphics;

import Models.Measurement;

import java.util.Vector;

/**
 * Stores lists of drawing instructions for lines and rectangles.
 */
public class GraphicsList {
    private final Vector<GraphicsInstructions> instructions;

    /**
     * Create an empty GraphicsList
     */
    public GraphicsList() {
        this.instructions = new Vector<>();
    }

    /**
     *
     * @return A Vector of Vectors of Instructions. The first element is for lines. The second element is for rectangles
     */
    public Vector<Vector<Vector<Integer>>> drawingInstructions() {
        Vector<Vector<Vector<Integer>>> result = new Vector<>();
        Vector<Vector<Integer>> lines = new Vector<>();
        Vector<Vector<Integer>> rectangles = new Vector<>();
        for (GraphicsInstructions graphic : this.instructions) {
            Class<? extends GraphicsInstructions> graphicClass = graphic.getClass();
            Vector<Integer> instructions = graphic.drawingInstructions();
            if (graphicClass == LineInstructions.class) {
                lines.add(instructions);
            }
            else if (graphicClass == RectangleInstructions.class) {
                rectangles.add(instructions);
            }
        }
        result.add(lines);
        result.add(rectangles);
        return result;
    }

    /**
     *
     * @param graphic - add a graphic to this list
     * @return this list
     */
    public GraphicsList addGraphic(GraphicsInstructions graphic) {
        this.instructions.add(graphic);
        return this;
    }

    /**
     *
     * @param drawingInstructions - A GraphicsList to merge with this one
     * @return this GraphicsList
     */
    public GraphicsList addGraphics(GraphicsList drawingInstructions){
        this.instructions.addAll(drawingInstructions.instructions);
        return this;
    }

    /**
     *
     * @param horizontal - how far to shift horizontally
     * @param vertical - how far to shift vertically
     * @return this GraphicsList
     */
    public GraphicsList shift(Measurement horizontal, Measurement vertical) {
        this.instructions.forEach(graphicsInstructions -> graphicsInstructions.shift(horizontal, vertical));
        return this;
    }
}

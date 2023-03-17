package Graphics;

import Models.Measurement;

import java.util.List;
import java.util.Vector;

/**
 * A Class used to store information required to draw a rectangle
 */
public class RectangleInstructions implements GraphicsInstructions {
    private final Measurement topLeftX;
    private final Measurement topLeftY;
    private final Measurement width;
    private final Measurement height;

    /**
     *
     * @param topLeftX - The x position of the top left corner of the rectangle
     * @param topLeftY - The y position of the top left corner of the rectangle
     * @param width - the width of the rectangle
     * @param height - the height of the rectangle
     */
    public RectangleInstructions(Measurement topLeftX, Measurement topLeftY, Measurement width, Measurement height) {
        // Clone the Measurements to ensure any changes to outside measurements won't affect this set of Instructions
        this.topLeftX = topLeftX.clone();
        this.topLeftY = topLeftY.clone();
        this.width = width.clone();
        this.height = height.clone();
    }

    /**
     *
     * @return A Vector of Integers (pixels) required to create this Rectangle
     */
    @Override
    public Vector<Integer> drawingInstructions() {
        return new Vector<>(List.of(this.topLeftX.numberOfPixels(), this.topLeftY.numberOfPixels(),
                this.width.numberOfPixels(), this.height.numberOfPixels()));
    }

    /**
     * Shifts the Rectangle top left corner horizontally and vertically
     * @param horizontal - How far to shift the Rectangle horizontally
     * @param vertical - How far to shift the Rectangle vertically
     * @return this Rectangle
     */
    @Override
    public GraphicsInstructions shift(Measurement horizontal, Measurement vertical) {
        this.topLeftX.add(horizontal);
        this.topLeftY.add(vertical);
        return this;
    }
}

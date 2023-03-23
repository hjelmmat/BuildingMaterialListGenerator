package Graphics;

import Models.Measurement;

import java.util.List;
import java.util.Vector;

/**
 * A Class used to store information required to draw a line
 */
public class LineInstructions implements GraphicsInstructions {
    private final Measurement startX;
    private final Measurement startY;
    private final Measurement endX;
    private final Measurement endY;

    /**
     *
     * @param startX - Start x position of the line
     * @param startY - Start y position of the line
     * @param endX - End x position of the line
     * @param endY - End y position of the line
     */
    public LineInstructions(Measurement startX, Measurement startY, Measurement endX, Measurement endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**
     *
     * @return A Vector of Integers (pixels) required to create this Line
     */
    @Override
    public Vector<Integer> drawingInstructions() {
        return new Vector<>(List.of(this.startX.numberOfPixels(), this.startY.numberOfPixels(),
                this.endX.numberOfPixels(), this.endY.numberOfPixels()));
    }

    /**
     * Shifts the Line start and end point horizontally and vertically
     * @param horizontal - How far to shift the line horizontally
     * @param vertical - How far to shift the line vertically
     * @return this Line
     */
    @Override
    public LineInstructions shift(Measurement horizontal, Measurement vertical) {
        return new LineInstructions(this.startX.add(horizontal), this.startY.add(vertical),this.endX.add(horizontal), this.endY.add(vertical));
    }
}

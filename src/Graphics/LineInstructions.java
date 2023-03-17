package Graphics;

import Models.Measurement;

import java.util.List;
import java.util.Vector;

/**
 * A Class used to store information required to draw a line
 */
public class LineInstructions implements GraphicsInstructions {
    private final Measurement startx;
    private final Measurement starty;
    private final Measurement endx;
    private final Measurement endy;

    /**
     *
     * @param startx - Start x position of the line
     * @param starty - Start y position of the line
     * @param endx - End x position of the line
     * @param endy - End y position of the line
     */
    public LineInstructions(Measurement startx, Measurement starty, Measurement endx, Measurement endy) {
        this.startx = startx.clone();
        this.starty = starty.clone();
        this.endx = endx.clone();
        this.endy = endy.clone();
    }

    /**
     *
     * @return A Vector of Integers (pixels) required to create this Line
     */
    @Override
    public Vector<Integer> drawingInstructions() {
        return new Vector<>(List.of(this.startx.numberOfPixels(), this.starty.numberOfPixels(),
                this.endx.numberOfPixels(), this.endy.numberOfPixels()));
    }

    /**
     * Shifts the Line start and end point horizontally and vertically
     * @param horizontal - How far to shift the line horizontally
     * @param vertical - How far to shift the line vertically
     * @return this Line
     */
    @Override
    public GraphicsInstructions shift(Measurement horizontal, Measurement vertical) {
        this.startx.add(horizontal);
        this.starty.add(vertical);
        this.endx.add(horizontal);
        this.endy.add(vertical);
        return this;
    }
}

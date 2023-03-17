package UI;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class WallGraphic extends Canvas {
    private Vector<Vector<Integer>> lines;
    private Vector<Vector<Integer>> rectangles;
    private final JFrame frame;
    private Dimension dimension;

    public WallGraphic() {
         this.frame = new JFrame("Wall");
        this.frame.add(this);
        this.lines = new Vector<>();
        this.rectangles = new Vector<>();
    }

    public void paint(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        this.rectangles.forEach((rectangle) -> g.drawRect(rectangle.get(0), rectangle.get(1), rectangle.get(2), rectangle.get(3)));
        this.frame.setMinimumSize(dimension);
    }

    public void setup(Vector<Vector<Integer>> lines, Vector<Vector<Integer>> rectangles, int maxX, int maxY) {
        this.lines = lines;
        this.rectangles = rectangles;
        this.dimension = new Dimension(maxX, maxY);
        this.frame.setVisible(true);
        this.paint(this.frame.getGraphics());
    }
}

package Graphics;


import Models.Measurement;

import java.util.Vector;

public interface GraphicsInstructions {
    Vector<Integer> drawingInstructions();
    GraphicsInstructions shift(Measurement horizontal, Measurement vertical);
}

package controllers;

import UI.WallGraphic;

import java.util.List;
import java.util.Vector;


public class WallController {

    public WallController(Vector<Vector<Vector<Integer>>> drawingInstructions, WallGraphic graphic) {
        Vector<Vector<Integer>> lines = drawingInstructions.get(0);
        Vector<Vector<Integer>> rectangles = drawingInstructions.get(1);
        Vector<Integer> maxLines = getMaxXAndY(lines);
        Vector<Integer> maxRectangles = getMaxXAndY(rectangles);

        // Set up the Graphic to have size equal to 100 pixels greater than the greatest X and Y elements.
        graphic.setup(lines, rectangles,
                Math.max(maxLines.get(0), maxRectangles.get(0)) + 100,
                Math.max(maxLines.get(1), maxRectangles.get(1)) + 100);
    }

    private static Vector<Integer> getMaxXAndY(Vector<Vector<Integer>> instructions) {
        int maxX = 0;
        int maxY = 0;
        for (Vector<Integer> instruction : instructions) {
            maxX = Math.max(instruction.get(0) + instruction.get(2), maxX);
            maxY = Math.max(instruction.get(1) + instruction.get(3), maxY);
        }
        return new Vector<>(List.of(maxX, maxY));
    }
}

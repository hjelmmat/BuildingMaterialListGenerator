package controllers;

import UI.WallGraphic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Vector;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WallControllerTest {
    @Mock
    WallGraphic myGraphic;

    @Test
    public void shouldCallGraphicSetup() {
        Vector<Vector<Vector<Integer>>> result = new Vector<>();
        Vector<Vector<Integer>> lines = new Vector<>();
        lines.add(new Vector<>(List.of(0,0,0,0)));
        result.add(lines);
        Vector<Vector<Integer>> rectangles = new Vector<>();
        rectangles.add(new Vector<>(List.of(1,2,1,2)));
        result.add(rectangles);
        new WallController(result, myGraphic);
        verify(myGraphic).setup(lines, rectangles, 102, 104);
    }
}
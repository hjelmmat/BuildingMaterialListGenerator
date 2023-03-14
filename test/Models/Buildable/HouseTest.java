package Models.Buildable;

import Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {
    @Test
    public void shouldAddWall() {
        Vector<Vector<String>> results = new Vector<>();
        results.add(new Vector<>(List.of("Material", "Quantity")));
        results.add(new Vector<>(List.of("24\" 2x4", "3")));
        results.add(new Vector<>(List.of("48\" 2x4", "3")));
        results.add(new Vector<>(List.of("10d nails", "30")));
        assertEquals(results, new House().addWall(new Measurement(24), new Measurement(30)).materials());
    }
}
package Models.Buildable;

import Models.Buildable.Installable.Layout;
import Models.Buildable.Installable.Stud;
import Models.Buildable.Material.Lumber;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {
    Measurement validMeasurement = new Measurement(100);
    Wall maxTwoStudWall = new Wall(new Measurement(17, Measurement.Fraction.ONE_HALF));
    Wall threeStudWall = new Wall(new Measurement(25));
    Wall fourStudWall = new Wall(new Measurement(40));

    @Test
    public void wallShouldThrowWhenLengthIsInvalid() {
        Measurement minimumLengthEdgeCase = new Measurement(2, Measurement.Fraction.FIFTEEN_SIXTEENTH);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                                        () -> new Wall(minimumLengthEdgeCase));
        String exceptionMessage = "length cannot be less than 3\", was 2-15/16\"";
        assertEquals(exceptionMessage, thrown.getMessage());
        assertDoesNotThrow(() -> new Wall(new Measurement(3)));
    }

    @Test
    public void wallShouldThrowWhenHeightIsInvalid() {
        Measurement minimumHeightEdgeCase = new Measurement(2, Measurement.Fraction.FIFTEEN_SIXTEENTH);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                                        () -> new Wall(this.validMeasurement, minimumHeightEdgeCase));
        String exceptionMessage = "height cannot be less than 4-1/2\", was 2-15/16\"";
        assertEquals(exceptionMessage, thrown.getMessage());
        Measurement minimumHeight = new Measurement(5);
        assertDoesNotThrow(() -> new Wall(this.validMeasurement, minimumHeight));
    }

    @Test
    public void wallShouldCreateWallWithDefaultHeight() {
        Vector<Vector<String>> results = new Vector<>();
        results.add(new Vector<>(List.of("Material", "Quantity")));
        results.add(new Vector<>(List.of("24\" 2x4", "3")));
        results.add(new Vector<>(List.of("92-5/8\" 2x4", "3")));
        results.add(new Vector<>(List.of("10d nails", "30")));
        assertEquals(results, new Wall(new Measurement(24)).materials());
    }

    @Test
    public void wallShouldCalculateCorrectStuds() throws IllegalArgumentException {
        Stud standardStud = new Stud();
        Layout minimumLayout = new Layout().addStudAt(new Measurement(0), standardStud)
                .addStudAt(new Measurement(1, Measurement.Fraction.ONE_HALF), standardStud);
        assertEquals(minimumLayout, new Wall(new Measurement(3)).layout());

        Measurement firstStud = new Measurement(0);
        Measurement secondStud = new Measurement(16);
        Layout twoStudLayout = new Layout().addStudAt(firstStud, standardStud).addStudAt(secondStud, standardStud);
        assertEquals(twoStudLayout, this.maxTwoStudWall.layout());

        Layout minThreeStudLayout = new Layout()
                .addStudAt(firstStud, standardStud)
                .addStudAt(secondStud, standardStud)
                .addStudAt(new Measurement(16, Measurement.Fraction.ONE_SIXTEENTH), standardStud);
        Wall minThreeStudWall = new Wall(new Measurement(17, Measurement.Fraction.NINE_SIXTEENTH));
        assertEquals(minThreeStudLayout, minThreeStudWall.layout());

        Layout threeStudLayout = new Layout().addStudAt(firstStud, standardStud)
                .addStudAt(secondStud, standardStud)
                .addStudAt(new Measurement(23, Measurement.Fraction.ONE_HALF), standardStud);
        assertEquals(threeStudLayout, this.threeStudWall.layout());

        Layout fourStudLayout = new Layout()
                .addStudAt(firstStud, standardStud)
                .addStudAt(secondStud, standardStud)
                .addStudAt(new Measurement(32), standardStud)
                .addStudAt(new Measurement(38, Measurement.Fraction.ONE_HALF), standardStud);
        assertEquals(fourStudLayout, this.fourStudWall.layout());
    }

    @Test
    public void wallShouldCalculateCorrectStudHeightForLayout() {
        Stud shortStud = new Stud(new Measurement(5), Lumber.Dimension.TWO_BY_FOUR);
        Layout shortLayout = new Layout().addStudAt(new Measurement(0), shortStud)
                .addStudAt(new Measurement(1, Measurement.Fraction.ONE_HALF), shortStud);
        assertEquals(shortLayout, new Wall(new Measurement(3), new Measurement(9, Measurement.Fraction.ONE_HALF)).layout());
    }
}

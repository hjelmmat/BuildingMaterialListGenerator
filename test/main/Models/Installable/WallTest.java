package main.Models.Installable;

import main.Models.Material.Lumber;
import main.Models.Material.MaterialList;
import main.Models.Material.Nails;
import main.Models.Measurement;
import org.junit.jupiter.api.Test;

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
        String exceptionMessage = "length cannot be less than 3\"; length was 2-15/16\"";
        assertEquals(exceptionMessage, thrown.getMessage());
        assertDoesNotThrow(() -> new Wall(new Measurement(3)));
    }

    @Test
    public void wallShouldThrowWhenHeightIsInvalid() {
        Measurement minimumHeightEdgeCase = new Measurement(2, Measurement.Fraction.FIFTEEN_SIXTEENTH);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                                        () -> new Wall(this.validMeasurement, minimumHeightEdgeCase));
        String exceptionMessage = "height cannot be less than 4-1/2\"; height was 2-15/16\"";
        assertEquals(exceptionMessage, thrown.getMessage());
        Measurement minimumHeight = new Measurement(5);
        assertDoesNotThrow(() -> new Wall(this.validMeasurement, minimumHeight));
    }

    @Test
    public void wallShouldCalculateCorrectNumberOfStuds() throws IllegalArgumentException {
        assertEquals(2, new Wall(new Measurement(3)).numberOfStuds());

        assertEquals(2, this.maxTwoStudWall.numberOfStuds());

        Wall minThreeStudWall = new Wall(new Measurement(17, Measurement.Fraction.NINE_SIXTEENTH));
        assertEquals(3, minThreeStudWall.numberOfStuds());

        assertEquals(3, this.threeStudWall.numberOfStuds());

        Measurement maxLengthForThreeStuds = new Measurement(33, Measurement.Fraction.ONE_HALF);
        assertEquals(3, new Wall(maxLengthForThreeStuds).numberOfStuds());

        assertEquals(4, this.fourStudWall.numberOfStuds());
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

    @Test
    public void wallShouldCreateMaterialList() {
        MaterialList result = new MaterialList().addMaterial(Nails.TEN_D, 20)
                .addMaterial(new Lumber(new Measurement(24), Lumber.Dimension.TWO_BY_FOUR), 3)
                .addMaterial(new Lumber(new Measurement(92, Measurement.Fraction.FIVE_EIGHTH), Lumber.Dimension.TWO_BY_FOUR), 2);
        assertEquals(result, new Wall(new Measurement(3)).material());
    }
}
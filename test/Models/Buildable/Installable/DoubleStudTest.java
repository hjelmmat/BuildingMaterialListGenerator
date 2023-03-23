package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleStudTest {
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    Measurement defaultLength = new Measurement(92, Measurement.Fraction.FIVE_EIGHTH);

    @Test
    public void shouldIncludeTwoStudsAndExtraNails() {
        Measurement length = new Measurement(12);
        MaterialList result = new MaterialList()
                .addMaterial(new Lumber(length, this.dimension), 2)
                .addMaterial(Nail.TEN_D, 10);
        assertEquals(result, new DoubleStud(length, this.dimension).materialList());

        Measurement slightlyLonger = new Measurement(12, Measurement.Fraction.ONE_SIXTEENTH);
        assertEquals(result.addMaterial(Nail.TEN_D, 2), new DoubleStud(slightlyLonger, this.dimension).materialList());
    }

    @Test
    public void shouldShowTwoStudsInDrawing() {
        Measurement zero = new Measurement(0);
        Measurement height = new Measurement(10);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, this.dimension.width, height))
                .addGraphic(new RectangleInstructions(this.dimension.width, zero, this.dimension.width, height));
        assertEquals(result.drawingInstructions(), new DoubleStud(height, this.dimension).graphicsList().drawingInstructions());
    }

    @Test
    public void shouldConstructFromStud() {
        DoubleStud result = new DoubleStud(this.defaultLength, this.dimension);
        DoubleStud fromStud = new DoubleStud(new Stud());
        assertEquals(result, fromStud);
        assertEquals(result.materialList(), fromStud.materialList());
    }

    @Test
    public void shouldNotEqualStud() {
        Stud stud = new Stud();
        assertNotEquals(stud, new DoubleStud(this.defaultLength, this.dimension));
    }

    @Test
    public void shouldReturnTotalWidth() {
        assertEquals(new Measurement(3), new DoubleStud(this.defaultLength, this.dimension).totalWidth());
    }
}
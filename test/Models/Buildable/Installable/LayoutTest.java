package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LayoutTest {
    Stud defaultStud = new Stud();
    Measurement firstPosition = new Measurement(0);
    Measurement secondPosition = new Measurement(1, Measurement.Fraction.ONE_HALF);
    Layout basicLayout = new Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud);

    @Test
    public void layoutShouldAddStudWithPositionCopy() {
        Measurement position = new Measurement(0);
        assertEquals(basicLayout, new Layout().addStudAt(position, this.defaultStud).
                addStudAt(position.add(this.secondPosition), this.defaultStud));
    }

    @Test
    public void layoutShouldReturnMaterials() {
        MaterialList result = new MaterialList().addMaterial(Nail.TEN_D, 12)
                .addMaterial(new Lumber(new Measurement(92), Lumber.Dimension.TWO_BY_FOUR), 2);
        assertEquals(result, this.basicLayout.materialList());
    }

    @Test
    public void layoutShouldDetermineEquals() {
        Layout testingLayout = new Layout().addStudAt(firstPosition, defaultStud).addStudAt(secondPosition, defaultStud);
        assertEquals(basicLayout, testingLayout);
    }

    @Test
    public void layoutShouldCreateGraphicsList() {
        Measurement width = Lumber.Dimension.TWO_BY_FOUR.width;
        Measurement zero = new Measurement(0);
        Measurement distance = new Measurement(10);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, width, distance))
                .addGraphic(new RectangleInstructions(distance, zero, width, distance));
        Stud stud = new Stud(distance, Lumber.Dimension.TWO_BY_FOUR);
        assertEquals(result.drawingInstructions(), new Layout().addStudAt(new Measurement(0), stud).addStudAt(distance, stud).drawingInstructions().drawingInstructions());
    }
}
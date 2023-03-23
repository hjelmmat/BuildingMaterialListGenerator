package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeaderTest {
    Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
    @Test
    public void shouldReturnMaterialList() {
        Measurement width = new Measurement(41);
        MaterialList result = new MaterialList()
                .addMaterial(new Lumber(width, Lumber.Dimension.TWO_BY_FOUR), 2)
                .addMaterial(new Lumber(width, Lumber.Dimension.TWO_BY_SIX), 2)
                .addMaterial(Nail.TEN_D, 48);
        assertEquals(result, new Header(width).materialList());
    }

    @Test
    public void shouldReturnGraphicsList() {
        Measurement zero = new Measurement(0);
        Measurement gap = new Measurement(41);
        GraphicsList result = new GraphicsList()
                .addGraphic(new RectangleInstructions(zero, zero, gap, dimension.width))
                .addGraphic(new RectangleInstructions(zero, dimension.width, gap, Lumber.Dimension.TWO_BY_SIX.height))
                .addGraphic(new RectangleInstructions(zero, dimension.width.clone().add(Lumber.Dimension.TWO_BY_SIX.height), gap, Lumber.Dimension.TWO_BY_SIX.height))
                .addGraphic(new RectangleInstructions(zero, dimension.width.clone().add(Lumber.Dimension.TWO_BY_SIX.height.clone().multiply(2)), gap, dimension.width));
        assertEquals(result.drawingInstructions(), new Header(gap).graphicsList().drawingInstructions());
    }

    @Test
    public void shouldReturnTotalWidth() {
        Measurement gapWidth = new Measurement(44);
        assertEquals(gapWidth, new Header(gapWidth).totalWidth());
    }

    @Test
    public void shouldReturnTotalHeight() {
        Measurement result = new Measurement(8, Measurement.Fraction.ONE_HALF);
        assertEquals(result, new Header(new Measurement(41)).totalHeight());
    }

}
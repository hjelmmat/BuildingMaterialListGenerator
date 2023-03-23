package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Graphics.RectangleInstructions;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Buildable.Material.Nail;
import Models.Measurement;

/**
 * A Header is a load bearing combination of studs to support weight above openings in wall, like doors and windows
 */
public class Header implements Installable{
    private final MaterialList materials;
    private final GraphicsList graphics;
    private final Measurement width;
    private final Measurement height;

    /**
     *
     * @param gapWidth - The size of the gap to cover with the Header. This includes the width of the studs the header
     *                 is placed on top of.
     */
    public Header(Measurement gapWidth) {
        Lumber.Dimension loadBearingDimension = Lumber.Dimension.TWO_BY_SIX;
        DoubleStud loadBearingStud = new DoubleStud(gapWidth, loadBearingDimension);
        Lumber.Dimension plateDimension = Lumber.Dimension.TWO_BY_FOUR;
        Plate plateStuds = new Plate(gapWidth, plateDimension);
        this.materials = new MaterialList()
                .addMaterials(loadBearingStud.materialList())
                // Both the top and bottom plates need to be attached to the header
                .addMaterials(plateStuds.materialList())
                .addMaterials(plateStuds.materialList())
                //Generally, plates don't need to be attached twice, but in this case, it should be attached to the
                // loadBearingStud and to the vertical studs.
                .addMaterial(Nail.TEN_D, 12);

        Measurement zero = new Measurement(0);
        // TODO: Add functionality to Graphics List to better deal with shifting and rotating.
        this.graphics = new GraphicsList()
                .addGraphics(plateStuds.graphicsList())
                .addGraphic(new RectangleInstructions(zero, plateDimension.width, gapWidth, loadBearingDimension.height))
                .addGraphic(new RectangleInstructions(zero, plateDimension.width.clone().add(loadBearingDimension.height), gapWidth, loadBearingDimension.height))
                .addGraphic(new RectangleInstructions(zero, plateDimension.width.clone().add(loadBearingDimension.height.clone().multiply(2)), gapWidth, plateDimension.width));

        this.width = gapWidth;
        this.height = loadBearingDimension.height.clone().add(plateDimension.width.clone().multiply(2));
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Header
     */
    @Override
    public Measurement totalWidth() {
        return this.width.clone();
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the height of this Header
     */
    @Override
    public Measurement totalHeight() {
        return this.height.clone();
    }

    /**
     *
     * @return - A {@link MaterialList} of {@link Models.Buildable.Material.Material} used to create this Header
     */
    @Override
    public MaterialList materialList() {
        return this.materials;
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Header
     */
    @Override
    public GraphicsList graphicsList() {
        return this.graphics;
    }
}
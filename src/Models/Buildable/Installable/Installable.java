package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Models.Buildable.Material.MaterialList;
import Models.Measurement;

public interface Installable {
    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Installable
     */
    Measurement totalWidth();

    /**
     *
     * @return - A copy of the {@link Measurement} of the height of the installable
     */
    Measurement totalHeight();

    /**
     *
     * @return - A {@link MaterialList} of {@link Models.Buildable.Material.Material} used to create this Installable
     */
    MaterialList materialList();

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Installable
     */
    GraphicsList graphicsList();
}

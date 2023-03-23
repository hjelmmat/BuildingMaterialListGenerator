package Models.Buildable.Installable;

import Models.Buildable.Material.Lumber;
import Models.Measurement;

public class KingStud extends DoubleStud {
    public KingStud(Measurement length, Lumber.Dimension dimension, Door.StandardDoor doorType) {
        super(length, dimension);
    }
}

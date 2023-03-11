package Models.Buildable;

import Models.Measurement;

import java.util.Vector;

public class House implements Buildable {
    private Wall wall;

    public House addWall(Measurement wallLength, Measurement wallHeight) throws IllegalArgumentException {
        this.wall = new Wall(wallLength, wallHeight);
        return this;
    }

    @Override
    public Vector<Vector<String>> materials() throws NullPointerException {
        return wall.materials();
    }
}

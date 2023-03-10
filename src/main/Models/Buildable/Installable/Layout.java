package main.Models.Buildable.Installable;

import main.Models.Buildable.Material.MaterialList;
import main.Models.Measurement;

import java.util.TreeMap;

/**
 * Class used to describe positions of studs. Essentially a c-style struct
 */
public class Layout implements Installable {
    TreeMap<Measurement, Stud> map;

    /**
     * Constructor to create an empty Layout
     */
    public Layout() {
        this.map = new TreeMap<>();
    }

    /**
     *
     * @param position - Position of the stud in the layout
     * @param stud - Stud that goes at the position
     * @return Updated Layout with new stud
     */
    public Layout addStudAt(Measurement position, Stud stud) {
        this.map.put(position.clone(), stud);
        return this;
    }

    /**
     *
     * @return The material required to install this layout.
     */
    @Override
    public MaterialList materialList() {
        MaterialList result = new MaterialList();
        this.map.forEach((k,v) -> result.addMaterials(v.materialList()));
        return result;
    }

    /**
     *
     * @param obj - the object to compare to this
     * @return if the object is the same or not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Layout l)) {
            return false;
        }
        return this.map.equals(l.map);
    }

    /**
     *
     * @return a hashcode for this Layout
     */
    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}

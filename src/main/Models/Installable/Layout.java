package main.Models.Installable;

import main.Models.Material.MaterialList;
import main.Models.Measurement;

import java.util.TreeMap;

/**
 * Class used to describe positions of studs. Essentially a c-style struct
 */
public class Layout extends TreeMap<Measurement, Stud> implements Installable {

    /**
     *
     * @param position - Position of the stud in the layout
     * @param stud - Stud that goes at the position
     * @return Updated Layout with new stud
     */
    public Layout addStudAt(Measurement position, Stud stud) {
        this.put(position.clone(), stud);
        return this;
    }

    /**
     *
     * @return The material required to install this layout.
     */
    @Override
    public MaterialList material() {
        MaterialList result = new MaterialList();
        this.forEach((k,v) -> result.addMaterials(v.material()));
        return result;
    }
}

package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Models.Buildable.Material.Lumber;
import Models.Buildable.Material.MaterialList;
import Models.Measurement;

/**
 * A class designed to store information about a door
 */
public class Door implements Installable {
    private Measurement totalHeight;
    private final Measurement totalWidth;
    private final StandardDoor type;
    private final Header header;
    private final Measurement topOfHeader;
    private Layout floorLayout;
    private final Layout crippleStuds;

    // Extra nails are needed to attach trimmers to kings and the edge cripple studs to kings.
    private int extraNails;
    private final static Lumber.Dimension studDimension = Lumber.Dimension.TWO_BY_FOUR;

    /**
     * Width and Heights of doors are inclusive of the lumber used to FRAME a door, not the final installed door. This
     * means a Standard Bedroom door, which has an 80" high installed door, actually requires 88 1/2" of framed space.
     * This is also true of the width.
     */
    public enum StandardDoor {
        Bedroom(new Measurement(36), new Measurement(80));

        public final Measurement openingWidth;
        public final Measurement openingHeight;

        StandardDoor(Measurement width, Measurement height) {
            // There should be an extra inch of space on both sides of the door frame
            this.openingWidth = new Measurement(2).add(width);

            // There should be an extra inch in height for the door frame
            this.openingHeight = new Measurement(1).add(height);
        }
    }

    public Door(StandardDoor ofType) {
        this.type = ofType;

        // Headers should be long enough to cover the gap and rest on two trimmers, which are 2x4s
        this.header = new Header(this.type.openingWidth.add(studDimension.width.multiply(2)));
        this.topOfHeader = this.header.totalHeight().add(this.type.openingHeight);

        // there will be 2 king and 2 jack studs all of 2x4 so that is part of the total width.
        this.totalWidth = this.type.openingWidth.add(studDimension.width.multiply(4));
        this.totalHeight = this.topOfHeader;
        this.floorLayout = this.floorLayout();

        // Trimmers need to be attached to King studs so add those extra nails
        this.extraNails = Plate.numberOfNails(this.type.openingHeight) * 2;
        this.crippleStuds = new Layout();
    }

    public Door() {
        this(StandardDoor.Bedroom);
    }

    private Layout floorLayout() {
        Lumber.Dimension dimension = Lumber.Dimension.TWO_BY_FOUR;
        Stud king = new Stud(this.totalHeight, dimension);
        Stud trimmer = new Stud(this.type.openingHeight, dimension);
        return new Layout()
                .addStudAt(new Measurement(0), king)
                .addStudAt(dimension.width, trimmer)
                .addStudAt(totalWidth.subtract(dimension.width.multiply(2)), trimmer)
                .addStudAt(totalWidth.subtract(dimension.width), king);
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the height of this Door
     */
    public Measurement totalHeight() {
        return this.totalHeight;
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Door
     */
    @Override
    public Measurement totalWidth() {
        return this.totalWidth;
    }

    /**
     *
     * @return - A {@link MaterialList} of {@link Models.Buildable.Material.Material} used to create this Door
     */
    @Override
    public MaterialList materialList() {
        return new MaterialList()
                .addMaterials(this.floorLayout.materialList())
                .addMaterials(this.header.materialList())
                .addMaterials(this.crippleStuds.materialList())
                .addMaterial(Stud.nailType, this.extraNails);
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Door
     */
    @Override
    public GraphicsList graphicsList() {
        return new GraphicsList()
                .addGraphics(this.floorLayout.graphicsList())
                .addGraphics(this.header.graphicsList().shift(studDimension.width, this.totalHeight.subtract(this.topOfHeader)))
                .addGraphics(this.crippleStuds.graphicsList());
    }

    /**
     * This method is intending to take a stud that was in a wall layout and move it into the Door's layout instead.
     * If the stud happens to conflict with a King stud of the Door, it will be moved next to it.
     * @param stud - A stud replaced by this door
     * @param location - The location of the stud relative to the start of the door
     * @return This Door
     */
    public Door addCrippleStud(Stud stud, Measurement location) throws Layout.InstallableLocationConflict {
        if (location.compareTo(this.totalWidth) > 0) {
            String error = String.format("Stud cannot be added at %s. Door is only %s long", location, this.totalWidth);
            throw new Layout.InstallableLocationConflict(error, location);
        }

        // If the cripple stud need to go before the first or after the last, it can be ignored as there is a king stud
        // there already
        Measurement firstCrippleStud = studDimension.width;
         Measurement lastCrippleStud = this.totalWidth().subtract(studDimension.width.multiply(2));
        if (location.compareTo(firstCrippleStud) <= 0 || location.compareTo(lastCrippleStud) >= 0)
        {
            return this;
        }
        // The king studs need to be updated to be taller
        if (stud.totalHeight().compareTo(this.totalHeight) > 0) {
            this.totalHeight = stud.totalHeight();
            this.floorLayout = this.floorLayout();
        }
        // Cripple studs should be as tall to take the space between the open
        Measurement crippleStudHeight = this.totalHeight().subtract(this.topOfHeader);
        Stud crippleStud = new Stud(crippleStudHeight, studDimension);

        // If there are any cripple studs that end up next to each other, the number of extra nails that need to be added
        // is below
        int crippleStudExtraNails = Plate.numberOfNails(crippleStudHeight);
        // There needs to be one above each trimmer
        if (this.crippleStuds.isEmpty()) {
            this.crippleStuds.addStudAt(firstCrippleStud, crippleStud);
            this.crippleStuds.addStudAt(lastCrippleStud, crippleStud);

            // These studs are attached to the king studs on the outside in the same manner as a plate, but are not
            // actually plates, so we need to manually add the extra nails
            this.extraNails += crippleStudExtraNails * 2;
        }
        try {
            this.crippleStuds.addStudAt(location, crippleStud);
        }
        /*
            There should only be two situations here:
             1. A stud that conflicts with the first or last one, then it needs to be added but shifted out of the way
             2. A stud that conflicts with any other stud, then the exception is valid and should be thrown again.
             A cripple stud interfering with the king stud should be handled above
         */
        catch (Layout.InstallableLocationConflict e) {
            if (e.conflict.equals(firstCrippleStud)) {
                this.crippleStuds.addStudAt(firstCrippleStud.add(crippleStud.totalWidth()), crippleStud);
                this.extraNails += crippleStudExtraNails;
            }
            else if (e.conflict.equals(lastCrippleStud)) {
                this.crippleStuds.addStudAt(lastCrippleStud.subtract(crippleStud.totalWidth()), crippleStud);
                this.extraNails += crippleStudExtraNails;
            }
            else {
                throw e;
            }
        }
        return this;
    }

    /**
     * Doors are only equal if the type of door and the tripe layout are the same
     * @param obj - the object to be compared
     * @return Indication if the two doors are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Door d)) {
            return false;
        }
        return this.type.equals(d.type) && this.crippleStuds.equals(d.crippleStuds);
    }

    /**
     *
     * @return hash code based on the type and tripleLayout
     */
    @Override
    public int hashCode() {
        return  this.type.hashCode() + this.crippleStuds.hashCode();
    }
}

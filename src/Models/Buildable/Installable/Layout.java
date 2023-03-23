package Models.Buildable.Installable;

import Graphics.GraphicsList;
import Models.Buildable.Material.MaterialList;
import Models.Measurement;

import java.util.*;

/**
 * Class used to describe positions of {@link Installable}. Essentially a c-style struct
 */
public class Layout implements Installable {
    private final TreeMap<Measurement, Stud> studs;
    private final TreeMap<Measurement, Door> doors;
    private Measurement tallestStudHeight;

    /**
     * An exception that is used when an {@link Installable} cannot be placed where requested
     */
    public static class InstallableLocationConflict extends IllegalArgumentException {
        public Measurement conflict;

        public InstallableLocationConflict(String s, Measurement conflict) {
            super(s);
            this.conflict = conflict.clone();
        }
    }

    /**
     * Constructor to create an empty Layout
     */
    public Layout() {
        this.studs = new TreeMap<>();
        this.doors = new TreeMap<>();
        this.tallestStudHeight = new Measurement(0);
    }

    /**
     *
     * @param position - Position of the stud in the layout
     * @param stud - Stud that goes at the position
     * @return Updated Layout with new stud
     */
    public Layout addStudAt(Measurement position, Stud stud) {
        this.detectConflicts(position, stud.totalWidth().add(position), this.studs, "Stud");
        this.studs.put(position.clone(), stud);

        // Keep track of the tallest studs, so we know how far to shift studs down in the graphics
        this.tallestStudHeight = this.tallestStudHeight.compareTo(stud.installedHeight) < 0 ? stud.installedHeight.clone() : this.tallestStudHeight;
        return this;
    }

    /**
     * Add a door to this layout. If the door position conflicts with any studs on the right or left edge, then it will
     * move those studs outside the door to retain proper layout minimum distances.
     * @param ofType - The type of {@link Models.Buildable.Installable.Door.StandardDoor} to add
     * @param atLocation - the Location to add the door
     * @return - Updated Layout with new Door
     * @throws InstallableLocationConflict - When the new door interacts with an already added Door or if a stud cannot
     * be moved
     */
    public Layout addDoorAt(Door ofType, Measurement atLocation) throws InstallableLocationConflict {
        Measurement doorEndLocation = atLocation.clone().add(ofType.totalWidth());
        if (doorEndLocation.compareTo(this.totalWidth()) > 0 ) {
            String error = String.format("Door cannot be added at %s. Layout is only %s long and door ends at %s",
                    atLocation,
                    this.totalWidth(),
                    doorEndLocation);
            throw new InstallableLocationConflict(error, atLocation);
        }
        this.detectConflicts(atLocation, doorEndLocation, this.doors, "Door");
        this.tryShiftOutsideStudsToValidPosition(atLocation, doorEndLocation, "Door");
        Object[] replacedStuds = this.studs.subMap(atLocation, doorEndLocation).keySet().toArray(); // TODO: Make this better
        for (Object location : replacedStuds ) {
            Measurement mLocation = (Measurement) location;
            Stud currentInstall = this.studs.get(mLocation);

            // Cripple studs need to be shifted into a layout that is relative to the left of the door
            ofType.addCrippleStud(currentInstall, mLocation.clone().subtract(atLocation));
            this.studs.remove(location);
        }
        this.doors.put(atLocation, ofType);
        return this;
    }

    /*
    If a stud is left of the start location then shift it farther left
    If a stud extends to the right outside the location from the inside, then shift it farther right
    This method is recursive to handle both the start and end locations
     */
    private void tryShiftOutsideStudsToValidPosition(Measurement startLocation, Measurement endLocation, String installableType)
            throws Measurement.InvalidMeasurementException {
        try {
            this.detectConflicts(startLocation, endLocation, this.studs, "studs");
        }
        catch (InstallableLocationConflict c) {
            Measurement conflictLocation = c.conflict;
            Stud studNeedingMoving = this.studs.get(conflictLocation);
            String error = String.format("%s cannot be added at %s. Stud at %s cannot be moved.",
                    installableType,
                    startLocation,
                    conflictLocation);
            InstallableLocationConflict potentialException = new InstallableLocationConflict(error, conflictLocation);

            /*
            Shift Stud Left
             */
            if (conflictLocation.compareTo(startLocation) < 0) {
                Measurement updatedLocation;
                try {
                    updatedLocation = startLocation.clone().subtract(studNeedingMoving.totalWidth());
                }
                catch (Measurement.InvalidMeasurementException e) {
                    throw potentialException;
                }
                this.studs.remove(conflictLocation);
                this.addStudAt(updatedLocation, studNeedingMoving);

                // Try again since it might have multiple conflicts
                this.tryShiftOutsideStudsToValidPosition(startLocation, endLocation, installableType);
            }
            /*
            If the Stud would move outside the width of the layout, we don't know if we should move it or not so throw
             */
            else if (studNeedingMoving.totalWidth().add(endLocation).compareTo(this.totalWidth()) > 0) {
                throw potentialException;
            }
            /*
            Shift Stud right
             */
            else if (studNeedingMoving.totalWidth().add(conflictLocation).compareTo(endLocation) > 0) {
                this.studs.remove(conflictLocation);
                this.addStudAt(endLocation, studNeedingMoving);
            }
            /*
            Otherwise, do nothing
             */
        }
    }

    private <T extends Installable> void detectConflicts(Measurement startLocation,
                                                         Measurement endLocation,
                                                         TreeMap<Measurement, T> currentInstallables,
                                                         String installableType) throws InstallableLocationConflict{
        /*
         Check to see if this location conflicts with any other installables assuming the installable with conflict
         with either the left or right edge
         */
        String error = String.format("%s cannot be added at %s. %s already is located at ",
                installableType,
                startLocation,
                installableType);
        Measurement leftOutside = currentInstallables.floorKey(startLocation);
        if (leftOutside != null
                && currentInstallables.get(leftOutside).totalWidth().add(leftOutside).compareTo(startLocation) > 0 ) {
            throw new InstallableLocationConflict(error + leftOutside, leftOutside);
        }
        Measurement rightInside = currentInstallables.floorKey(endLocation);
        if (rightInside != null
                && rightInside.compareTo(startLocation) >= 0
                && rightInside.compareTo(endLocation) < 0 ) {
            throw new InstallableLocationConflict(error + rightInside, rightInside);
        }
    }

    /**
     *
     * @return - A copy of the {@link Measurement} of the width of this Layout
     */
    @Override
    public Measurement totalWidth() {
        Map.Entry<Measurement, Stud> lastStud = this.studs.lastEntry();
        Measurement greatestStudWidth = lastStud != null
                ? lastStud.getKey().clone().add(lastStud.getValue().totalWidth())
                : new Measurement(0);
        Map.Entry<Measurement, Door> lastDoor = this.doors.lastEntry();
        Measurement greatestDoorWidth = lastDoor != null
                ? lastDoor.getKey().clone().add(lastDoor.getValue().totalWidth())
                : new Measurement(0);
        return greatestStudWidth.greaterValue(greatestDoorWidth).clone();
    }

    /**
     *
     * @return - A {@link MaterialList} of {@link Models.Buildable.Material.Material} used to create this Layout
     */
    @Override
    public MaterialList materialList() {
        MaterialList result = new MaterialList();
        this.studs.forEach((k,v) -> result.addMaterials(v.materialList()));
        this.doors.forEach((k,v) -> result.addMaterials(v.materialList()));
        return result;
    }

    /**
     *
     * @return - A {@link GraphicsList} of {@link Graphics.GraphicsInstructions} used to draw this Layout
     */
    @Override
    public GraphicsList graphicsList() {
        GraphicsList result = new GraphicsList();
        for (Measurement location : this.studs.keySet()) {
            Stud stud = this.studs.get(location);
            // Each stud needs to be shifted down a difference of the tallest stud and its height so the bottoms
            // are the same
            Measurement verticalShift = new Measurement(0).add(this.tallestStudHeight.clone().subtract(stud.installedHeight));
            result.addGraphics(stud.graphicsList().shift(location, verticalShift));
        }
        this.doors.forEach((location, door) -> result.addGraphics(door.graphicsList().shift(location, new Measurement(0))));
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
        return this.studs.equals(l.studs) && this.doors.equals(l.doors);
    }

    /**
     *
     * @return a hashcode for this Layout
     */
    @Override
    public int hashCode() {
        return this.studs.hashCode() * this.doors.hashCode();
    }

    /**
     *
     * @return - If there are any {@link Installable} in this Layout
     */
    public boolean isEmpty() {
        return this.studs.isEmpty() && this.doors.isEmpty();
    }
}

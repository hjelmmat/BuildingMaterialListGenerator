package main.Models.Buildable.Material;

/**
 * Nails used as material
 */
public enum Nail implements Material{
    TEN_D("10d");

    private final String niceString;

    Nail(String niceString) {
        this.niceString = niceString;
    }

    /**
     *
     * @return a nice String representation of the Nail
     */
    @Override
    public String toString() {
        return this.niceString + " nails";
    }
}

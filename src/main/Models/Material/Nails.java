package main.Models.Material;

/**
 * Nails used as material
 */
public enum Nails implements Material{
    TEN_D("10d");

    private final String niceString;

    Nails(String niceString) {
        this.niceString = niceString;
    }

    @Override
    public String toString() {
        return this.niceString + " nails";
    }
}

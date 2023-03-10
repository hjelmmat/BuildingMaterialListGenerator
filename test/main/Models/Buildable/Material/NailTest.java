package main.Models.Buildable.Material;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NailTest {
    @Test
    public void shouldReturnNiceString() {
        String result = "10d nails";
        assertEquals(result, Nail.TEN_D.toString());
    }

}
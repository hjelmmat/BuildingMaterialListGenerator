package main.Models.Material;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NailsTest {

    @Test
    public void shouldReturnNiceString() {
        String result = "10d nails";
        assertEquals(result, Nails.TEN_D.toString());
    }

}
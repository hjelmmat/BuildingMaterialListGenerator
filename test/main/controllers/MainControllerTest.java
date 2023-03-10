package main.controllers;

import static org.mockito.Mockito.*;

import UI.MainFrame;
import main.Models.Buildable.House;
import main.Models.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;


/*
Setting up the mocks causes to run the test slowly. If you comment out the timings in 'setup()' then you'll see
a substantial delay before the setup runs and the rest of the test is instantaneous.
 */
@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    Vector<String> headers = new Vector<>(List.of("Material", "Quantity"));
    Vector<Vector<String>> material = new Vector<>();

    @Mock
    MainFrame myFrame;
    @Mock
    House myHouse;

    @BeforeEach
    public void setup() {
//        System.out.println(System.nanoTime());
        when(myFrame.getHeightText()).thenReturn("10");
        when(myFrame.getHeightFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        when(myFrame.getLengthText()).thenReturn("10");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);

        when(myHouse.addWall(new Measurement(10), new Measurement(10))).thenReturn(myHouse);

        material.clear();
        material.add(new Vector<>(List.of("24\" 2x4", "5")));
        material.add(new Vector<>(List.of("10d nails", "20")));
        Vector<Vector<String>> result = new Vector<>();
        result.add(headers);
        result.addAll(material);
        when(myHouse.materials()).thenReturn(result);
//        System.out.println(System.nanoTime());
    }

    @Test
    public void shouldUpdateTable() {
        MainController test = new MainController(myFrame, myHouse);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).updateTable(material, headers);
    }
}
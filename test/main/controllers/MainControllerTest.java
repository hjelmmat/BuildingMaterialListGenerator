package main.controllers;

import static org.mockito.Mockito.*;

import UI.MainFrame;
import main.Models.Material.Lumber;
import main.Models.Material.Nail;
import main.Models.Measurement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;


@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    @Mock
    private MainFrame myFrame;

    @AfterEach
    public void setup() {
        reset(myFrame);
    }

    /*
     This test is very slow for some reason. Maybe something to do with making the Wall, but the entirety of the
     WallTest class is faster than the execution of this one test.
     */
    @Test
    public void shouldUpdateTable() {
        MainController test = new MainController(myFrame);
        when(myFrame.getHeightText()).thenReturn("10");
        when(myFrame.getHeightFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        when(myFrame.getLengthText()).thenReturn("10");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        Vector<Vector<Object>> result = new Vector<>();
        result.add(new Vector<>(List.of(new Lumber(new Measurement(24), Lumber.Dimension.TWO_BY_FOUR), 5)));
        result.add(new Vector<>((List.of(Nail.TEN_D, 20))));
        verify(myFrame).updateTable(argThat(arg -> arg.getDataVector().equals(result)));
    }
}
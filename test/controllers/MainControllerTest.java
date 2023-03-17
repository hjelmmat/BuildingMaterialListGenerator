package controllers;

import static org.mockito.Mockito.*;

import UI.MainFrame;
import Models.Buildable.House;
import Models.Measurement;
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
        reset(myFrame);
//        System.out.println(System.nanoTime());
    }

    @Test
    public void shouldCalculateMaterials() {
        when(myFrame.getHeightText()).thenReturn("10");
        when(myFrame.getHeightFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        when(myFrame.getLengthText()).thenReturn("10");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);

        when(myHouse.addWall(new Measurement(10), new Measurement(10))).thenReturn(myHouse);
        when(myHouse.drawingInstructions()).thenReturn(null);
        MainController test = new MainController(myFrame, myHouse);

        material.add(new Vector<>(List.of("24\" 2x4", "5")));
        material.add(new Vector<>(List.of("10d nails", "20")));
        Vector<Vector<String>> result = new Vector<>();
        result.add(headers);
        result.addAll(material);
        when(myHouse.materials()).thenReturn(result);

        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).updateTable(material, headers);
        verify(myFrame).showGraphic(null);
    }

    @Test
    public void shouldDisplayErrorForEmptyLengths() {
        MainController test = new MainController(myFrame, myHouse);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).postError("Length cannot be empty");
    }

    @Test
    public void shouldDisplayErrorForNegativeLengths() {
        when(myFrame.getLengthText()).thenReturn("-1");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        MainController test = new MainController(myFrame, myHouse);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).postError("Length measurement cannot be less than 0, was -1");
    }

    @Test
    public void shouldDisplayErrorForEmptyHeight() {
        when(myFrame.getLengthText()).thenReturn("10");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        MainController test = new MainController(myFrame, myHouse);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).postError("Height cannot be empty");
    }

    @Test
    public void shouldDisplayErrorForNegativeHeights() {
        when(myFrame.getLengthText()).thenReturn("10");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        when(myFrame.getHeightText()).thenReturn("-1");
        when(myFrame.getHeightFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        MainController test = new MainController(myFrame, myHouse);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).postError("Height measurement cannot be less than 0, was -1");
    }

    @Test
    public void shouldDisplayErrorForInvalidWall() {
        // Setup valid values for the UI fields
        when(myFrame.getHeightText()).thenReturn("10");
        when(myFrame.getHeightFractionValue()).thenReturn(Measurement.Fraction.ZERO);
        when(myFrame.getLengthText()).thenReturn("10");
        when(myFrame.getLengthFractionValue()).thenReturn(Measurement.Fraction.ZERO);

        // throw an error anyway
        String testError = "Throw anyway";
        when(myHouse.addWall(new Measurement(10), new Measurement(10))).thenThrow(new IllegalArgumentException(testError));
        MainController test = new MainController(myFrame, myHouse);
        test.calculateMaterials(new ActionEvent(0, 0, ""));
        verify(myFrame).postError(testError);
    }
}
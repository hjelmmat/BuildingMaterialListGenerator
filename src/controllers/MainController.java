package controllers;

import Models.Buildable.House;
import Models.Measurement;
import UI.MainFrame;

import java.awt.event.ActionEvent;
import java.util.Vector;

public class MainController {
    private final MainFrame view;
    private final House house;


    public MainController(MainFrame view, House house) {
        this.view = view;
        this.view.addCalculateListener(this::calculateMaterials);

        this.house = house;
    }

    public void calculateMaterials(ActionEvent event) {
        Measurement length = verifyMeasurement(this.view.getLengthText(), this.view.getLengthFractionValue(), "Length");
        if (length == null) { return;}
        Measurement height = verifyMeasurement(this.view.getHeightText(), this.view.getHeightFractionValue(), "Height");
        if (height == null) { return; }

        try {
            this.house.addWall(length, height);
        }
        catch (IllegalArgumentException e) {
            this.view.postError(e.getMessage());
            return;
        }
        Vector<Vector<String>> material = this.house.materials();

        // The first element of the vector from Material is the title and everything else is the data
        this.view.updateTable(new Vector<>(material.subList(1, material.size())), material.get(0));
        this.view.showGraphic(this.house.drawingInstructions());
    }

    private Measurement verifyMeasurement(String intValue, Measurement.Fraction fractionValue, String measurementType) {
        try {
            return new Measurement(Integer.parseInt(intValue), fractionValue);
        }
        catch (Measurement.InvalidMeasurementException e) {
            this.view.postError(measurementType + " " + e.getMessage());
            return null;
        }
        catch (NumberFormatException e) {
            this.view.postError(measurementType + " cannot be empty");
            return null;
        }
    }
}

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
        Measurement length;
        try {
            length = new Measurement(Integer.parseInt(this.view.getLengthText()), this.view.getLengthFractionValue());
        }
        catch (Measurement.InvalidMeasurementException e) {
            this.view.postError("Length " + e.getMessage());
            return;
        }
        catch (NumberFormatException e) {
            this.view.postError("Length cannot be empty");
            return;
        }

        Measurement height;
        try {
            height = new Measurement(Integer.parseInt(this.view.getHeightText()), this.view.getHeightFractionValue());
        }
        catch (Measurement.InvalidMeasurementException e) {
            this.view.postError("Height " + e.getMessage());
            return;
        }
        catch (NumberFormatException e) {
            this.view.postError("Height cannot be empty");
            return;
        }

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
    }
}

package main.controllers;

import UI.MainFrame;
import main.Models.Buildable.House;
import main.Models.Measurement;

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

    public void calculateMaterials(ActionEvent e) {
        Measurement length = new Measurement(Integer.parseInt(this.view.getLengthText()), this.view.getLengthFractionValue());
        Measurement height = new Measurement(Integer.parseInt(this.view.getHeightText()), this.view.getHeightFractionValue());
        this.house.addWall(length, height);
        Vector<Vector<String>> material = this.house.materials();

        // The first element of the vector from Material is the title and everything else is the data
        this.view.updateTable(new Vector<>(material.subList(1, material.size())), material.get(0));
    }
}

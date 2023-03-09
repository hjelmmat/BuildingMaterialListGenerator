package main.controllers;

import UI.MainFrame;
import main.Models.Installable.Wall;
import main.Models.Measurement;

public class MainController {
    private final MainFrame view;


    public MainController(MainFrame view) {
        this.view = view;
        this.view.addCalculateListener(e -> calculateMaterials());
    }

    public void calculateMaterials() {
        Measurement height = new Measurement(Integer.parseInt(this.view.getHeightText()), this.view.getHeightFractionValue());
        Measurement length = new Measurement(Integer.parseInt(this.view.getLengthText()), this.view.getLengthFractionValue());
        this.view.updateTable(new Wall(length, height).material().asTableModel());
    }
}

package main.controllers;

import UI.MainFrame;
import main.Models.Installable.Wall;
import main.Models.Measurement;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class MainController {
    private final MainFrame view;


    public MainController(MainFrame view) {
        this.view = view;
        this.view.addCalculateListener(e -> calculateMaterials());
    }

    public void calculateMaterials() {
        Measurement height = new Measurement(Integer.parseInt(this.view.getHeightText()), this.view.getHeightFractionValue());
        Measurement length = new Measurement(Integer.parseInt(this.view.getLengthText()), this.view.getLengthFractionValue());
        DefaultTableModel model = new DefaultTableModel(new Wall(length, height).material().asVector(),
                new Vector<>(List.of("Material", "Quantity")));
        this.view.updateTable(model);
    }
}

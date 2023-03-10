package main.controllers;

import UI.MainFrame;
import main.Models.Buildable.Wall;
import main.Models.Measurement;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class MainController {
    private final MainFrame view;


    public MainController(MainFrame view) {
        this.view = view;
        this.view.addCalculateListener(this::calculateMaterials);
    }

    public void calculateMaterials(ActionEvent e) {
        Measurement height = new Measurement(Integer.parseInt(this.view.getHeightText()), this.view.getHeightFractionValue());
        Measurement length = new Measurement(Integer.parseInt(this.view.getLengthText()), this.view.getLengthFractionValue());
        Vector<Vector<String>> material = new Wall(length, height).material();

        // The first element of the vector from Material is the title and everything else is the data
        this.view.updateTable(new DefaultTableModel(new Vector<>(material.subList(1, material.size())), material.get(0)));
    }
}

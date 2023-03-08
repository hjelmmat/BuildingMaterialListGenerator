package main.controllers;

import UI.MainFrame;
import main.Models.Installable.Wall;
import main.Models.Material.MaterialList;
import main.Models.Measurement;

import javax.swing.table.DefaultTableModel;

public class MainController {
    private final MainFrame view;


    public MainController(MainFrame view) {
        this.view = view;
        this.view.addCalculateListener(e -> calculateMaterials());
    }

    public void calculateMaterials() {
        Measurement height = new Measurement(Integer.parseInt(this.view.getHeightText()), this.view.getHeightFractionValue());
        Measurement length = new Measurement(Integer.parseInt(this.view.getLengthText()), this.view.getLengthFractionValue());
        MaterialList materials = new Wall(length, height).material();
        Object[] columnNames = {"Material", "Quantity"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        materials.forEach((k,v) -> model.addRow(new Object[] {k.toString(), v}));
        this.view.updateTable(model);
    }
}

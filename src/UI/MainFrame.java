package UI;

import Models.Measurement;
import controllers.WallController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainFrame {
    private JFrame frame;
    private JLabel heightLB;
    private JLabel lengthLB;
    private JScrollPane resultsSP;
    private JTable resultsTB;
    private JPanel wallMaterials;
    private JFormattedTextField heightIntTF;
    private JComboBox<Measurement.Fraction> heightFractionCB; // TODO: Consider making a list of Strings instead
    private JFormattedTextField lengthIntTF;
    private JComboBox<Measurement.Fraction> lengthFractionCB;
    private JButton calculateBtn;

    public MainFrame(){
        this.frame = new JFrame();
        this.frame.setContentPane(wallMaterials);
        this.frame.setTitle("Wall Materials");
        this.frame.setSize(750, 300);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        DefaultComboBoxModel<Measurement.Fraction> heightFraction = new DefaultComboBoxModel<>(Measurement.Fraction.values());
        this.heightFractionCB.setModel(heightFraction);
        this.heightFractionCB.setEditable(false);

        DefaultComboBoxModel<Measurement.Fraction> lengthFraction = new DefaultComboBoxModel<>(Measurement.Fraction.values());
        this.lengthFractionCB.setModel(lengthFraction);
        this.lengthFractionCB.setEditable(false);
    }

    public void addCalculateListener(ActionListener actionListener) {
        calculateBtn.addActionListener(actionListener);
    }

    public void updateTable(Vector<Vector<String>> modelData, Vector<String> dataHeaders) {
        this.resultsTB.setModel(new DefaultTableModel(modelData, dataHeaders));
    }

    public void postError(String errorMessage) {
        JOptionPane.showMessageDialog(this.frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getHeightText(){ return this.heightIntTF.getText(); };
    public Measurement.Fraction getHeightFractionValue() { return (Measurement.Fraction) this.heightFractionCB.getSelectedItem(); }
    public String getLengthText(){ return this.lengthIntTF.getText(); };
    public Measurement.Fraction getLengthFractionValue() { return (Measurement.Fraction) this.lengthFractionCB.getSelectedItem(); }

    public void showGraphic(Vector<Vector<Vector<Integer>>> drawingInstructions) {
        new WallController(drawingInstructions, new WallGraphic());
    }
}


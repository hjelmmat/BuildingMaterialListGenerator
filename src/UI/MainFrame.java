package UI;

import main.Models.Measurement;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame{
    private JLabel heightLB;
    private JLabel lengthLB;
    private JScrollPane resultsSP;
    private JTable resultsTB;
    private JPanel wallMaterials;
    private JTextField heightIntTF;
    private JComboBox<Measurement.Fraction> heightFractionCB;
    private JTextField lengthIntTF;
    private JComboBox<Measurement.Fraction> lengthFractionCB;
    private JButton calculateBtn;

    public MainFrame(){
        this.setContentPane(wallMaterials);
        this.setTitle("Wall Materials");
        this.setSize(750, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

    public void updateTable(TableModel model) {
        this.resultsTB.setModel(model);
    }


    public String getHeightText(){ return this.heightIntTF.getText(); };
    public Measurement.Fraction getHeightFractionValue() { return (Measurement.Fraction) this.heightFractionCB.getSelectedItem(); }
    public String getLengthText(){ return this.lengthIntTF.getText(); };
    public Measurement.Fraction getLengthFractionValue() { return (Measurement.Fraction) this.lengthFractionCB.getSelectedItem(); }
}


package rybas.gui;

import rybas.controller.Utils;
import rybas.models.functions.CustomFunction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JPanel panel4Draw;
    private JTextField equationTextField;
    private JButton submitButton;
    private JScrollPane paramsScrollPane;
    private JTable parametersTable;
    private JButton plusParamButton;
    private JButton minusParamButton;
    private DrawPanel drawPanel;
    private LinkedHashMap<String, Double> parameters;
    private DefaultTableModel parametersTableModel;

    public MainWindow() {
        paramsScrollPane.setLayout(new ScrollPaneLayout());
        parameters = new LinkedHashMap<>();

        this.getContentPane().add(mainPanel);

        plusParamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parametersTableModel.addRow(new String[]{"", ""});
            }
        });

        minusParamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parametersTableModel.getRowCount() != 0)
                    parametersTableModel.removeRow(parametersTableModel.getRowCount() - 1);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LinkedHashMap<String, Double> parameters = new LinkedHashMap<>();
                    for (int i = 0; i < parametersTableModel.getRowCount(); i++) {
                        if (parametersTableModel.getValueAt(i, 0) != null &&
                                parametersTableModel.getValueAt(i, 1) != null) {
                            String parameter = parametersTableModel.getValueAt(i, 0).toString();
                            try {
                                Double value = Double.parseDouble(parametersTableModel.getValueAt(i, 1).toString());
                                parameters.put(parameter, value);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                    String equation = Utils.changeParameters(equationTextField.getText(), parameters);
                    drawPanel.setFunction(new CustomFunction(equation, parameters));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        drawPanel = new DrawPanel();
        panel4Draw = drawPanel;
        drawPanel.setMinimumSize(panel4Draw.getMinimumSize());

        parametersTableModel = new DefaultTableModel(new String[][]{
        }, new String[]{"Parameters",
                "Values"});
        parametersTable = new JTable(parametersTableModel);
    }
}

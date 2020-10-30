package rybas.gui;

import rybas.controller.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedHashMap;

public class MainWindow extends JFrame implements KeyListener {
    private JPanel mainPanel;
    private JPanel panel4Draw;
    private JSpinner nOfParamsSpinner;
    private JTextField equationTextField;
    private JButton submitButton;
    private JScrollPane paramsScrollPane;
    private JTextArea paramsTextArea;
    private DrawPanel drawPanel;
    private LinkedHashMap<String, Double> parameters;

    public MainWindow() {
        this.setFocusable(true);
        this.addKeyListener(this);
        drawPanel = new DrawPanel();
        drawPanel.setMinimumSize(panel4Draw.getMinimumSize());
        panel4Draw.setLayout(new GridLayout());
        paramsScrollPane.setLayout(new ScrollPaneLayout());
        panel4Draw.add(drawPanel);
        parameters = new LinkedHashMap<>();

        this.getContentPane().add(mainPanel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    parameters = Utils.parseParameters(paramsTextArea.getText());
                    String equation = Utils.changeParameters(equationTextField.getText(), parameters);
                    drawPanel.drawFunc(equation, parameters);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
//            if (!equationTextField.getText().equals(""))
//                drawPanel.drawFunc(equationTextField.getText());
//        }
    }
}

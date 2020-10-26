package rybas.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JTextField equationTextField;
    private DrawPanel drawPanel;
    private JButton submitButton;

    public MainWindow() {
        drawPanel = new DrawPanel();
        mainPanel.setLayout(new BorderLayout());

        submitButton = new JButton("Submit");
        equationTextField = new JTextField();

        mainPanel.add(drawPanel);
        mainPanel.add(equationTextField, BorderLayout.SOUTH);
        mainPanel.add(submitButton, BorderLayout.LINE_END);
        this.getContentPane().add(mainPanel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.drawFunc(equationTextField.getText());
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}

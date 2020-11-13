package rybas;

import rybas.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        var mw = new MainWindow();
        mw.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mw.setSize(new Dimension(1920, 1080));
        mw.setVisible(true);
    }
}

package rybas.gui;

import com.udojava.evalex.Expression;
import rybas.controller.ScreenConvertor;
import rybas.linedrawers.DDALineDrawer;
import rybas.linedrawers.LineDrawer;
import rybas.models.Line;
import rybas.models.pixeldrawers.BufferedImagePixelDrawer;
import rybas.models.pixeldrawers.PixelDrawer;
import rybas.models.points.RealPoint;
import rybas.models.points.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private ArrayList<Line> lines = new ArrayList<>();
    private ScreenConvertor sc = new ScreenConvertor(
            -2, 2, 4, 4, 800, 600
    );
    private Line xAxis = new Line(-sc.getScreenW(), 0, sc.getScreenW(), 0);
    private Line yAxis = new Line(0, -sc.getScreenH(), 0, sc.getScreenH());
    private LineDrawer ld;
    private String equation = "sin(x)";

    public DrawPanel() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D bi_g = bi.createGraphics();
        PixelDrawer pd = new BufferedImagePixelDrawer(bi);
        ld = new DDALineDrawer(pd);
        sc.setScreenH(getHeight());
        sc.setScreenW(getWidth());
        bi_g.fillRect(0, 0, getWidth(), getHeight());
        bi_g.setColor(Color.black);
        drawAxes(ld);
        drawAll(ld);
        bi_g.dispose();
        g.drawImage(bi, 0, 0, null);
    }

    private void drawAll(LineDrawer ld) {
        for (Line l :
                lines) {
            ld.drawLine(sc.realToScreen(l.getP1()), sc.realToScreen(l.getP2()));
        }
        if (currentLine != null)
            ld.drawLine(sc.realToScreen(currentLine.getP1()), sc.realToScreen(currentLine.getP2()));

        drawAxes(ld);
        drawFunc(equation);
    }

    public void drawFunc(String equation) {
        this.equation = equation;
        RealPoint p1 = null, p2;
        for (double i = -sc.getScreenW(); i < sc.getScreenW(); i++) {
            BigDecimal result = new Expression(equation).with("x", BigDecimal.valueOf(i)).eval();
            if (i == -sc.getScreenW()) p1 = new RealPoint(i, result.doubleValue());
            p2 = new RealPoint(i, result.doubleValue());
            ld.drawLine(sc.realToScreen(p1), sc.realToScreen(p2));
            p1 = p2;
        }
        repaint();
    }

    private void drawAxes(LineDrawer ld) {
        ld.drawLine(sc.realToScreen(xAxis.getP1()), sc.realToScreen(xAxis.getP2()));
        ld.drawLine(sc.realToScreen(yAxis.getP1()), sc.realToScreen(yAxis.getP2()));
        for (Line l : lines) {
            ld.drawLine(sc.realToScreen(l.getP1()),
                    sc.realToScreen(l.getP2()));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ScreenPoint current = new ScreenPoint(e.getX(), e.getY());
        if (currentLine != null) {
            currentLine.setP2(sc.screenToReal(current));
        }

        if (prevDrag != null) {
            ScreenPoint delta = new ScreenPoint(current.getX() - prevDrag.getX(), current.getY() - prevDrag.getY());
            RealPoint deltaToReal = sc.screenToReal(delta);
            RealPoint zeroToReal = sc.screenToReal(new ScreenPoint(0, 0));
            RealPoint vector = new RealPoint(deltaToReal.getX() - zeroToReal.getX(), deltaToReal.getY() - zeroToReal.getY());
            sc.setX(sc.getX() - vector.getX());
            sc.setY(sc.getY() - vector.getY());
            prevDrag = current;
        }
        repaint();
    }

    private ScreenPoint prevDrag;
    private Line currentLine;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
            prevDrag = new ScreenPoint(e.getX(), e.getY());
        else if (e.getButton() == MouseEvent.BUTTON1)
            currentLine = new Line(sc.screenToReal(new ScreenPoint(e.getX(), e.getY())), sc.screenToReal(new ScreenPoint(e.getX(), e.getY())));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3)
            prevDrag = null;
        if (e.getButton() == MouseEvent.BUTTON1) {
            lines.add(currentLine);
            currentLine = null;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int ticks = e.getWheelRotation();
        double scale = 1;
        double cf = ticks > 0 ? 1.1 : 0.9;
        for (int i = 0; i <= Math.abs(ticks); i++) {
            scale *= cf;
        }
        sc.setW(sc.getW() * scale);
        sc.setH(sc.getH() * scale);
        repaint();
    }
}

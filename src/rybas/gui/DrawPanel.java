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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private ArrayList<Line> lines = new ArrayList<>();
    private ScreenConvertor sc = new ScreenConvertor(
            -5, 5, 10, 10, 800, 800
    );
    private Line xAxis = new Line(-sc.getScreenW(), 0, sc.getScreenW(), 0);
    private Line yAxis = new Line(0, -sc.getScreenH(), 0, sc.getScreenH());
    private DDALineDrawer ld;
    private String equation = "0";
    private LinkedHashMap<String, Double> parameters = new LinkedHashMap<>();

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
        drawUnitSegments(bi_g);
        drawAll(ld);
        bi_g.dispose();
        g.drawImage(bi, 0, 0, null);
    }

    private void drawAll(DDALineDrawer ld) {
        for (Line l :
                lines) {
            ld.drawLine(sc.realToScreen(l.getP1()), sc.realToScreen(l.getP2()));
        }
        if (currentLine != null)
            ld.drawLine(sc.realToScreen(currentLine.getP1()), sc.realToScreen(currentLine.getP2()));

        drawAxes(ld);
        drawGrid(ld);
        drawFunc(equation, parameters);
    }

    public void drawFunc(String equation, LinkedHashMap<String, Double> parameters) {
        this.equation = equation;
        this.parameters = parameters;

        double step = 0.01;
        for (double x1 = -sc.getW() + sc.getX(); x1 < sc.getW() + sc.getX(); x1 += step) {
            double x2 = x1 + step;
            try {
                BigDecimal result1 = new Expression(equation).with("x", BigDecimal.valueOf(x1)).eval();
                BigDecimal result2 = new Expression(equation).with("x", BigDecimal.valueOf(x2)).eval();
                ScreenPoint p1 = sc.realToScreen(new RealPoint(x1, result1.doubleValue()));
                ScreenPoint p2 = sc.realToScreen(new RealPoint(x2, result2.doubleValue()));
                ld.drawLine(p1, p2);
            } catch (Expression.ExpressionException | NumberFormatException ignored) {
            }
        }
        repaint();
    }

    private void drawGrid(DDALineDrawer ld) {
        ld.setColor(new Color(178, 178, 178));
        double step = sc.getW() / 10;
        for (double i = step; i <= sc.getW() + Math.abs(sc.getX()); i += step) {
            ScreenPoint p1 = sc.realToScreen(new RealPoint(i, sc.getH()));
            ScreenPoint p2 = sc.realToScreen(new RealPoint(i, -sc.getH()));
            ld.drawLine(p1, p2);
            p1 = sc.realToScreen(new RealPoint(-i, sc.getH()));
            p2 = sc.realToScreen(new RealPoint(-i, -sc.getH()));
            ld.drawLine(p1, p2);
        }

        step = sc.getH() / 10;
        for (double i = step; i <= sc.getH() + Math.abs(sc.getY()); i += step) {
            ScreenPoint p1 = sc.realToScreen(new RealPoint(sc.getW(), i));
            ScreenPoint p2 = sc.realToScreen(new RealPoint(-sc.getW(), i));
            ld.drawLine(p1, p2);
            p1 = sc.realToScreen(new RealPoint(sc.getW(), -i));
            p2 = sc.realToScreen(new RealPoint(-sc.getW(), -i));
            ld.drawLine(p1, p2);
        }
        ld.setColor(Color.BLACK);
    }

    private void drawUnitSegments(Graphics g) {
        g.setColor(Color.BLACK);
        double step = sc.getW() / 10;

        for (double x = 0; x <= sc.getW() + Math.abs(sc.getX()); x += step) {
            ScreenPoint point = sc.realToScreen(new RealPoint(x, 0));
            ScreenPoint oppositePoint = sc.realToScreen(new RealPoint(-x, 0));
            if (step >= 1) {
                g.drawString(String.valueOf((int) x), point.getX(), point.getY());
                g.drawString(String.valueOf((int) -x), oppositePoint.getX(), oppositePoint.getY());
            } else {
                String pattern = "#.#####";
                DecimalFormat f = new DecimalFormat(pattern);
                String value = f.format(x).equals("0") ? "0.00001" : f.format(x);
                g.drawString(value, point.getX(), point.getY());
                g.drawString(value, oppositePoint.getX(), oppositePoint.getY());
            }
        }

        step = sc.getH() / 10;
        for (double y = 0; y <= sc.getH() + Math.abs(sc.getY()); y += step) {
            ScreenPoint point = sc.realToScreen(new RealPoint(0, y));
            ScreenPoint oppositePoint = sc.realToScreen(new RealPoint(0, -y));
            if (step >= 1) {
                g.drawString(String.valueOf((int) y), point.getX(), point.getY());
                g.drawString(String.valueOf((int) -y), oppositePoint.getX(), oppositePoint.getY());
            } else {
                String pattern = "#.#####";
                DecimalFormat f = new DecimalFormat(pattern);
                String value = f.format(y).equals("0") ? "0.00001" : f.format(y);
                g.drawString(value, point.getX(), point.getY());
                g.drawString(value, oppositePoint.getX(), oppositePoint.getY());
            }
        }
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
            xAxis = new Line(-sc.getW() + sc.getX(), 0, sc.getW() + sc.getX(), 0);
            yAxis = new Line(0, -sc.getH() + sc.getY(), 0, sc.getH() + sc.getY());
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
        sc.setX(sc.getX() * scale);
        sc.setY(sc.getY() * scale);
        xAxis = new Line(-sc.getW() + sc.getX(), 0, sc.getW() + sc.getX(), 0);
        yAxis = new Line(0, -sc.getH() + sc.getY(), 0, sc.getH() + sc.getY());
        repaint();
    }
}

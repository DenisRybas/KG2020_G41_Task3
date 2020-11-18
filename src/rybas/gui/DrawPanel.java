package rybas.gui;

import rybas.controller.ScreenConvertor;
import rybas.models.CoordinateSystem;
import rybas.models.Line;
import rybas.models.RecountType;
import rybas.models.functions.CustomFunction;
import rybas.models.functions.IFunction;
import rybas.models.linedrawers.DDALineDrawer;
import rybas.models.linedrawers.LineDrawer;
import rybas.models.pixeldrawers.BufferedImagePixelDrawer;
import rybas.models.pixeldrawers.PixelDrawer;
import rybas.models.points.RealPoint;
import rybas.models.points.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private ArrayList<Line> lines = new ArrayList<>();
    private ScreenConvertor sc = new ScreenConvertor(
            -5, 5, 10, 10, 1920, 1080
    );

    private DDALineDrawer ld;
    private IFunction function;
    //    private LinkedHashMap<String, Double> parameters = new LinkedHashMap<>();
    private CoordinateSystem coordinateSystem;

    public void setFunction(IFunction function) {
        this.function = function;
        repaint();
    }

    public DrawPanel() {
        coordinateSystem = new CoordinateSystem(sc);
        function = new CustomFunction("0", null);
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
        sc.setScreenW(getWidth());
        sc.setScreenH(getHeight());
        bi_g.fillRect(0, 0, getWidth(), getHeight());
        bi_g.setColor(Color.black);
        coordinateSystem.setSc(sc);
        coordinateSystem.drawUnitSegments(bi_g);
        coordinateSystem.drawAxes(ld);
        coordinateSystem.drawGrid(ld);
        drawFunc(sc, ld);
        bi_g.dispose();
        g.drawImage(bi, 0, 0, null);
    }

    public void drawFunc(ScreenConvertor sc, LineDrawer ld) {
        //TODO: Вынести предыдущий результат, гипербола, iFunction (x, map parameters), drawfunc(iFunc, sc, ld), делать repaint, когда что-то меняем, параметры в табилце
        function.drawFunc(sc, ld);
        //TODO: Бесконечные линии, разрывы, step drawUnitSegments
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
            coordinateSystem.setXAxis(new Line(-sc.getW() + sc.getX(), 0, sc.getW() + sc.getX(), 0));
            coordinateSystem.setYAxis(new Line(0, -sc.getH() + sc.getY(), 0, sc.getH() + sc.getY()));
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
        double cf = ticks > 0 ? 2 : 0.5;
        scale *= cf;
        if (cf > 1)
            coordinateSystem.recountStep(RecountType.INCREASE);
        else
            coordinateSystem.recountStep(RecountType.DECREASE);
        sc.setW(sc.getW() * scale);
        sc.setH(sc.getH() * scale);
        sc.setX(sc.getX() * scale);
        sc.setY(sc.getY() * scale);
        sc.setScale(sc.getScale() * scale);
        coordinateSystem.setXAxis(new Line(-sc.getW() + sc.getX(), 0, sc.getW() + sc.getX(), 0));
        coordinateSystem.setYAxis(new Line(0, -sc.getH() + sc.getY(), 0, sc.getH() + sc.getY()));
        repaint();
    }
}

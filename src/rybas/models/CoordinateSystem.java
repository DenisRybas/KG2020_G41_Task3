package rybas.models;

import rybas.controller.ScreenConvertor;
import rybas.models.linedrawers.DDALineDrawer;
import rybas.models.linedrawers.LineDrawer;
import rybas.models.points.RealPoint;
import rybas.models.points.ScreenPoint;

import java.awt.*;

public class CoordinateSystem {
    private ScreenConvertor sc;
    private Line xAxis;
    private Line yAxis;
    private double step = 1;

    public CoordinateSystem(ScreenConvertor sc) {
        this.sc = sc;
        xAxis = new Line(-sc.getScreenW(), 0, sc.getScreenW(), 0);
        yAxis = new Line(0, -sc.getScreenH(), 0, sc.getScreenH());
    }

    public void setSc(ScreenConvertor sc) {
        this.sc = sc;
    }

    public void setXAxis(Line xAxis) {
        this.xAxis = xAxis;
    }

    public void setYAxis(Line yAxis) {
        this.yAxis = yAxis;
    }

    public void drawGrid(DDALineDrawer ld) {
        ld.setColor(new Color(178, 178, 178));
        float step = (float) sc.getW() / 20;

        for (double i = step; i <= sc.getW() + Math.abs(sc.getX()); i += step) {
            ScreenPoint p1 = sc.realToScreen(new RealPoint(i, sc.getH() + sc.getY()));
            ScreenPoint p2 = sc.realToScreen(new RealPoint(i, -(sc.getH() - sc.getY())));
            ld.drawLine(p1, p2);
            p1 = sc.realToScreen(new RealPoint(-i, sc.getH() + sc.getY()));
            p2 = sc.realToScreen(new RealPoint(-i, -(sc.getH() - sc.getY())));
            ld.drawLine(p1, p2);
        }

        for (double i = step; i <= sc.getH() + Math.abs(sc.getY()); i += step) {
            ScreenPoint p1 = sc.realToScreen(new RealPoint(sc.getW() + sc.getX(), i));
            ScreenPoint p2 = sc.realToScreen(new RealPoint(-(sc.getW() - sc.getX()), i));
            ld.drawLine(p1, p2);
            p1 = sc.realToScreen(new RealPoint(sc.getW() + sc.getX(), -i));
            p2 = sc.realToScreen(new RealPoint(-(sc.getW() - sc.getX()), -i));
            ld.drawLine(p1, p2);
        }
        ld.setColor(Color.BLACK);
    }

    public void drawUnitSegments(Graphics g) {
        g.setColor(Color.BLACK);
        float step = (float) sc.getW() / 20;
        float unitStep = 0;
        for (float x = 0; x <= sc.getW() + Math.abs(sc.getX()); x += step) {
            ScreenPoint point = sc.realToScreen(new RealPoint(x, 0));
            ScreenPoint oppositePoint = sc.realToScreen(new RealPoint(-x, 0));
            String value = String.valueOf(unitStep);
            if (unitStep > 1000000) value = String.format("%2.1e", unitStep);
            else if (unitStep < 0.00005) value = String.format("%2.2e", unitStep);
            if (x == 0.0) value = "0";
            g.drawString(value, point.getX(), point.getY());
            g.drawString(value, oppositePoint.getX(), oppositePoint.getY());
            unitStep += this.step;
        }

        unitStep = 0;
        for (float y = 0; y <= sc.getH() + Math.abs(sc.getY()); y += step) {
            ScreenPoint point = sc.realToScreen(new RealPoint(0, y));
            ScreenPoint oppositePoint = sc.realToScreen(new RealPoint(0, -y));
            String value = String.valueOf(unitStep);
            if (unitStep > 10000000) value = String.format("%2.1e", unitStep);
            else if (unitStep < 0.00005) value = String.format("%2.2e", unitStep);
            if (y == 0.0) value = "0";
            g.drawString(value, point.getX(), point.getY());
            g.drawString(value, oppositePoint.getX(), oppositePoint.getY());
            unitStep += this.step;
        }
    }

    public void recountStep(RecountType type) {
        double newStep;
        if (type == RecountType.DECREASE) {
            if (this.step >= 1)
                for (int i = 0; ; i++) {
                    for (int j = 0; j <= i + 1; j++) {
                        newStep = Math.pow(5, i);
                        newStep *= Math.pow(2, j);
                        if (newStep * 2 > this.step) {
                            this.step = newStep / 2;
                            return;
                        }
                    }
                }
            if (this.step < 1)
                for (int i = 0; ; i--) {
                    for (int j = 0; j >= i - 1; j--) {
                        newStep = Math.pow(5, i);
                        newStep *= Math.pow(2, j);
                        if (newStep * 2 < this.step) {
                            this.step = newStep / 2;
                            return;
                        }
                    }
                }
        } else {
            if (this.step >= 1)
                for (int i = 0; ; i++) {
                    for (int j = 0; j <= i + 1; j++) {
                        newStep = Math.pow(5, i);
                        newStep *= Math.pow(2, j);
                        if (newStep > this.step) {
                            this.step = newStep;
                            return;
                        }
                    }
                }
            if (this.step < 1)
                for (int i = 0; ; i--) {
                    for (int j = 0; j >= i - 1; j--) {
                        newStep = Math.pow(5, i);
                        newStep *= Math.pow(2, j);
                        if (newStep / 2 < this.step) {
                            this.step = newStep * 2;
                            return;
                        }
                    }
                }
        }
    }

    public void drawAxes(LineDrawer ld) {
        ld.drawLine(sc.realToScreen(xAxis.getP1()), sc.realToScreen(xAxis.getP2()));
        ld.drawLine(sc.realToScreen(yAxis.getP1()), sc.realToScreen(yAxis.getP2()));
    }
}

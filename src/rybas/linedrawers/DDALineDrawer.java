package rybas.linedrawers;

import rybas.models.pixeldrawers.PixelDrawer;
import rybas.models.points.ScreenPoint;

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private PixelDrawer pd;
    private Color c;
    public DDALineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    {
        c = Color.BLACK;
    }

    public void setColor(Color c) {
        this.c = c;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2) {
        int x2 = p2.getX();
        int x1 = p1.getX();
        int y2 = p2.getY();
        int y1 = p1.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        if (Math.abs(dx) > Math.abs(dy)) {
            if (x1 > x2) {
                int tmp = x1; x1 = x2; x2 = tmp;
                tmp = y1; y1 = y2; y2 = tmp;
            }
            double k = dy / dx;
            for (int j = x1; j <= x2; j++) {
                double i = k * (j - x1) + y1;
                pd.setPixel(j, (int) i, c);
            }
        } else {
            if (y1 > y2) {
                int tmp = x1; x1 = x2; x2 = tmp;
                tmp = y1; y1 = y2; y2 = tmp;
            }
            double kObr = dx / dy;
            for (int i = y1; i <= y2; i++) {
                double j = kObr * (i - y1) + x1;
                pd.setPixel((int) j, i, c);
            }
        }
    }
}

package rybas.models.linedrawers;

import rybas.models.points.ScreenPoint;

public interface LineDrawer {
    void drawLine(ScreenPoint p1, ScreenPoint p2);
}

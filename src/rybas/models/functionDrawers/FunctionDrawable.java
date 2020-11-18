package rybas.models.functionDrawers;

import com.udojava.evalex.Expression;
import rybas.controller.ScreenConvertor;
import rybas.models.functions.IFunction;
import rybas.models.linedrawers.LineDrawer;
import rybas.models.points.RealPoint;
import rybas.models.points.ScreenPoint;

import java.math.BigDecimal;

public interface FunctionDrawable {
    default void drawFunc(IFunction func, ScreenConvertor sc, LineDrawer ld) {
        double step = sc.getW() / 1000;
        for (double x1 = -sc.getW() + sc.getX(); x1 < sc.getW() + sc.getX(); x1 += step) {
            double x2 = x1 + step;
            try {
                BigDecimal result1 = func.evaluate(BigDecimal.valueOf(x1));
                BigDecimal result2 = func.evaluate(BigDecimal.valueOf(x2));

                ScreenPoint p1 = sc.realToScreen(new RealPoint(x1, result1.doubleValue()));
                ScreenPoint p2 = sc.realToScreen(new RealPoint(x2, result2.doubleValue()));
                ld.drawLine(p1, p2);
            } catch (Expression.ExpressionException | NumberFormatException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

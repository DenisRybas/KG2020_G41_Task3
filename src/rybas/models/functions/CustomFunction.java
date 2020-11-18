package rybas.models.functions;

import com.udojava.evalex.Expression;
import rybas.controller.ScreenConvertor;
import rybas.controller.Utils;
import rybas.models.linedrawers.LineDrawer;
import rybas.models.points.RealPoint;
import rybas.models.points.ScreenPoint;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomFunction implements IFunction {
    private String equation;
    private LinkedHashMap<String, Double> parameters;
    private Expression expression;

    public CustomFunction(String equation, LinkedHashMap<String, Double> parameters) {
        this.equation = equation;
        this.parameters = parameters;
        expression = new Expression(Utils.changeParameters(this.equation, this.parameters));
    }

    public void setEquation(String equation) {
        this.equation = equation;
        this.expression = new Expression(Utils.changeParameters(this.equation, this.parameters));
    }

    public void setParameters(LinkedHashMap<String, Double> parameters) {
        this.parameters = parameters;
        this.expression = new Expression(Utils.changeParameters(this.equation, this.parameters));
    }

    public String getEquation() {
        return equation;
    }

    public Map<String, Double> getParameters() {
        return parameters;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return expression.with("x", x).eval();
    }

    @Override
    public void drawFunc(ScreenConvertor sc, LineDrawer ld) {
        double step = sc.getW() / 1000;
        for (double x1 = -sc.getW() + sc.getX(); x1 < sc.getW() + sc.getX(); x1 += step) {
            double x2 = x1 + step;
            try {
                BigDecimal result1 = this.evaluate(BigDecimal.valueOf(x1));
                BigDecimal result2 = this.evaluate(BigDecimal.valueOf(x2));

                ScreenPoint p1 = sc.realToScreen(new RealPoint(x1, result1.doubleValue()));
                ScreenPoint p2 = sc.realToScreen(new RealPoint(x2, result2.doubleValue()));
                ld.drawLine(p1, p2);
            } catch (Expression.ExpressionException | NumberFormatException | ArithmeticException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

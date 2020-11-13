package rybas.models.functions;

import com.udojava.evalex.Expression;
import rybas.controller.Utils;

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
}

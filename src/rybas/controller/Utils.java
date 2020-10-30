package rybas.controller;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    public static LinkedHashMap<String, Double> parseParameters(String parametersStr) {
        if (parametersStr.isEmpty()) return null;
        LinkedHashMap<String, Double> parameters = new LinkedHashMap<>();
        for (String param :
                parametersStr.split("\n")) {
            boolean isBeforeEquality = true;
            String key = null;
            double value = 0.0;
            for (String p : param.split(" ")) {
                if (p.equals("=")) isBeforeEquality = false;

                if (!p.equals("=")) {
                    if (isBeforeEquality)
                        key = p;
                    else value = Double.parseDouble(p);
                }
                parameters.put(key, value);
            }
        }
        return parameters;
    }

    public static String changeParameters(String equation, LinkedHashMap<String, Double> parameters) {
        if (parameters == null) return equation;
        for (Map.Entry<String, Double> parameter :
                parameters.entrySet()) {
            equation = equation.replaceAll(parameter.getKey(), parameter.getValue().toString());
        }
        return equation;
    }
}

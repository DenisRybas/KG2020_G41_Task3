package rybas.models.functions;

import rybas.controller.ScreenConvertor;
import rybas.models.linedrawers.LineDrawer;

import java.math.BigDecimal;

public interface IFunction {
    default BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    default void drawFunc(ScreenConvertor sc, LineDrawer ld) {

    }
}

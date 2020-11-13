package rybas.models.functions;

import java.math.BigDecimal;

public interface IFunction {
    default BigDecimal evaluate(BigDecimal x) {
        return null;
    }
}

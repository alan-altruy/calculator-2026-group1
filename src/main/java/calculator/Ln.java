package calculator;

import calculator.value.Value;
import java.util.List;

public class Ln extends UnaryOperation {
    public Ln(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "ln"; }
    public Ln(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "ln"; }

    @Override
    public Value unOp(Value v) { return v.ln(); }
    @Override
    public int getPrecedence() { return 4; }
}

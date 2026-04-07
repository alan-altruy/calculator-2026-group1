package calculator;

import calculator.value.Value;
import java.util.List;

public class Factorial extends UnaryOperation {
    public Factorial(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "!"; }
    public Factorial(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "!"; }

    @Override
    public Value unOp(Value v) { return v.fact(); }
    @Override
    public int getPrecedence() { return 4; }
}

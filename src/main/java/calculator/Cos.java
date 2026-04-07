package calculator;

import calculator.value.Value;
import java.util.List;

public class Cos extends UnaryOperation {
    public Cos(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "cos"; }
    public Cos(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "cos"; }

    @Override
    public Value unOp(Value v) { return v.cos(); }
    @Override
    public int getPrecedence() { return 4; }
}

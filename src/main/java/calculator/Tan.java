package calculator;

import calculator.value.Value;
import java.util.List;

public class Tan extends UnaryOperation {
    public Tan(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "tan"; }
    public Tan(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "tan"; }

    @Override
    public Value unOp(Value v) { return v.tan(); }
    @Override
    public int getPrecedence() { return 4; }
}

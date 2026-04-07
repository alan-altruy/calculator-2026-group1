package calculator;

import calculator.value.Value;
import java.util.List;

public class Sin extends UnaryOperation {
    public Sin(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "sin"; }
    public Sin(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "sin"; }

    @Override
    public Value unOp(Value v) { return v.sin(); }
    @Override
    public int getPrecedence() { return 4; }
}

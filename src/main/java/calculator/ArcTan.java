package calculator;

import calculator.value.Value;
import java.util.List;

public class ArcTan extends UnaryOperation {
    public ArcTan(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "arctan"; }
    public ArcTan(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "arctan"; }

    @Override
    public Value unOp(Value v) { return v.arctan(); }
    @Override
    public int getPrecedence() { return 4; }
}

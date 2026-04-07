package calculator;

import calculator.value.Value;
import java.util.List;

public class ArcSin extends UnaryOperation {
    public ArcSin(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "arcsin"; }
    public ArcSin(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "arcsin"; }

    @Override
    public Value unOp(Value v) { return v.arcsin(); }
    @Override
    public int getPrecedence() { return 4; }
}

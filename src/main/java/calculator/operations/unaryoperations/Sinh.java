package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

public class Sinh extends UnaryOperation {
    public Sinh(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "sinh"; }
    public Sinh(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "sinh"; }

    @Override
    public Value unOp(Value v) { return v.sinh(); }
    @Override
    public int getPrecedence() { return 4; }
}

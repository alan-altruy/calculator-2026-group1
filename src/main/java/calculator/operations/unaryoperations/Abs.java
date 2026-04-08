package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

public class Abs extends UnaryOperation {
    public Abs(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "|x|"; }
    public Abs(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "|x|"; }

    @Override
    public Value unOp(Value v) { return v.abs(); }
    @Override
    public int getPrecedence() { return 4; }
}

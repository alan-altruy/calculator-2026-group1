package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

public class Cosh extends UnaryOperation {
    public Cosh(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "cosh"; }
    public Cosh(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "cosh"; }

    @Override
    public Value unOp(Value v) { return v.cosh(); }
    @Override
    public int getPrecedence() { return 4; }
}

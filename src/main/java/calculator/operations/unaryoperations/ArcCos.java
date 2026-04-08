package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

public class ArcCos extends UnaryOperation {
    public ArcCos(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "arccos"; }
    public ArcCos(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "arccos"; }

    @Override
    public Value unOp(Value v) { return v.arccos(); }
    @Override
    public int getPrecedence() { return 4; }
}

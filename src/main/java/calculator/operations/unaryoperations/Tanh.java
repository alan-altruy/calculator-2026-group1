package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

public class Tanh extends UnaryOperation {
    public Tanh(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "tanh"; }
    public Tanh(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "tanh"; }

    @Override
    public Value unOp(Value v) { return v.tanh(); }
    @Override
    public int getPrecedence() { return 4; }
}

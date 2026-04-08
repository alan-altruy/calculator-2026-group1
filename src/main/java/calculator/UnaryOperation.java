package calculator;

import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.operations.Operation;
import calculator.value.Value;
import java.util.List;

/**
 * Abstract class representing a unary arithmetic operation (e.g. sin, cos, ln).
 */
public abstract class UnaryOperation extends Operation {

    protected UnaryOperation(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        if (elist.size() != 1) {
            throw new IllegalConstruction("Unary operation requires exactly one argument");
        }
    }

    protected UnaryOperation(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        if (elist.size() != 1) {
            throw new IllegalConstruction("Unary operation requires exactly one argument");
        }
    }

    @Override
    public Value op(Value l, Value r) {
        return unOp(l);
    }

    public abstract Value unOp(Value v);
}

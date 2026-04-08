package calculator.operations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Modulo operation
 */
public class Mod extends Operation {

    public Mod(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        symbol = "mod";
    }

    public Mod(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "mod";
    }

    @Override
    public Value op(Value l, Value r) {
        return l.mod(r);
    }

    @Override
    public int getPrecedence() {
        return 2;
    }
}

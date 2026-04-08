package calculator.operations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Integer Division operation
 */
public class IntDiv extends Operation {

    public IntDiv(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        symbol = "//";
    }

    public IntDiv(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "//";
    }

    @Override
    public Value op(Value l, Value r) {
        return l.intDiv(r);
    }

    @Override
    public int getPrecedence() {
        return 2;
    }
}

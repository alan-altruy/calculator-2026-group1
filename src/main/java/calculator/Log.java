package calculator;

import calculator.value.Value;
import java.util.List;

public class Log extends UnaryOperation {
    public Log(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "log"; }
    public Log(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "log"; }

    @Override
    public Value unOp(Value v) { return v.log(); }
    @Override
    public int getPrecedence() { return 4; }
}

package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the logarithm operation.
 */
public class Log extends UnaryOperation {

    /**
     * Constructor for Log operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Log(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "log"; }

    /**
     * Constructor for Log operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Log(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "log"; }

    /**
     * Applies the logarithm operation to the given value.
     * @param v The value to which the logarithm operation will be applied
     * @return The result of applying the logarithm operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.log(); }

    /**
     * Returns the precedence of the logarithm operation.
     * @return The precedence level of the logarithm operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the natural logarithm operation.
 */
public class Ln extends UnaryOperation {

    /**
     * Constructor for Ln operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Ln(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "ln"; }

    /**
     * Constructor for Ln operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Ln(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "ln"; }

    /**
     * Applies the natural logarithm operation to the given value.
     * @param v The value to which the natural logarithm operation will be applied
     * @return The result of applying the natural logarithm operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.ln(); }

    /**
     * Returns the precedence of the natural logarithm operation.
     * @return The precedence level of the natural logarithm operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

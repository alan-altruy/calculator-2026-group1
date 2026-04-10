package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the factorial operation.
 */
public class Factorial extends UnaryOperation {

    /**
     * Constructor for Factorial operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Factorial(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "!"; }

    /**
     * Constructor for Factorial operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Factorial(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "!"; }

    /**
     * Applies the factorial operation to the given value.
     * @param v The value to which the factorial operation will be applied
     * @return The result of applying the factorial operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.fact(); }

    /**
     * Returns the precedence of the factorial operation.
     * @return The precedence level of the factorial operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

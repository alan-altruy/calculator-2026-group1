package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the hyperbolic sine operation.
 */
public class Sinh extends UnaryOperation {

    /**
     * Constructor for Sinh operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Sinh(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "sinh"; }

    /**
     * Constructor for Sinh operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Sinh(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "sinh"; }

    /**
     * Applies the hyperbolic sine operation to the given value.
     * @param v The value to which the hyperbolic sine operation will be applied
     * @return The result of applying the hyperbolic sine operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.sinh(); }

    /**
     * Returns the precedence of the hyperbolic sine operation.
     * @return The precedence level of the hyperbolic sine operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

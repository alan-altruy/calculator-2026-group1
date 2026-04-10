package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the hyperbolic tangent operation.
 */
public class Tanh extends UnaryOperation {

    /**
     * Constructor for Tanh operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Tanh(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "tanh"; }

    /**
     * Constructor for Tanh operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public Tanh(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "tanh"; }

    /**
     * Applies the hyperbolic tangent operation to the given value.
     * @param v The value to which the hyperbolic tangent operation will be applied
     * @return The result of applying the hyperbolic tangent operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.tanh(); }

    /**
     * Returns the precedence of the hyperbolic tangent operation.
     * @return The precedence level of the hyperbolic tangent operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

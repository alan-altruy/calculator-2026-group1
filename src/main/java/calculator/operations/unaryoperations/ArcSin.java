package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the arc sine operation.
 */
public class ArcSin extends UnaryOperation {

    /**
     * Constructor for ArcSin operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public ArcSin(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "arcsin"; }

    /**
     * Constructor for ArcSin operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public ArcSin(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "arcsin"; }

    /**
     * Applies the arc sine operation to the given value.
     * @param v The value to which the arc sine operation will be applied
     * @return The result of applying the arc sine operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.arcsin(); }

    /**
     * Returns the precedence of the arc sine operation.
     * @return The precedence level of the arc sine operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

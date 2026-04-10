package calculator.operations.unaryoperations;

import calculator.Expression;
import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import calculator.value.Value;
import java.util.List;

/**
 * Class representing the arc tangent operation.
 */
public class ArcTan extends UnaryOperation {

    /**
     * Constructor for ArcTan operation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public ArcTan(List<Expression> elist) throws IllegalConstruction { super(elist); symbol = "arctan"; }

    /**
     * Constructor for ArcTan operation with specified notation.
     * @param elist List of expressions (should contain exactly one expression for unary operation)
     * @param n Notation to be used for this operation
     * @throws IllegalConstruction if the number of expressions is not exactly one
     */
    public ArcTan(List<Expression> elist, Notation n) throws IllegalConstruction { super(elist, n); symbol = "arctan"; }

    /**
     * Applies the arc tangent operation to the given value.
     * @param v The value to which the arc tangent operation will be applied
     * @return The result of applying the arc tangent operation to the given value
     */
    @Override
    public Value unOp(Value v) { return v.arctan(); }
    
    /**
     * Returns the precedence of the arc tangent operation.
     * @return The precedence level of the arc tangent operation
     */
    @Override
    public int getPrecedence() { return 4; }
}

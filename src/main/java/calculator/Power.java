package calculator;

import calculator.value.Value;
import java.util.List;

/** This class represents the arithmetic power operation "**".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Divides
 * @see Minus
 * @see Times
 * @see Plus
 */
public class Power extends Operation {

    /**
     * Class constructor specifying a number of Expressions to exponentiate.
     * @param elist The list of Expressions to exponentiate
     * @throws IllegalConstruction   If an empty list of expressions if passed as parameter
     */
    public Power(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        symbol = "**";
        neutral = 1;
    }

    /**
     * Class constructor specifying a number of Expressions to exponentiate,
     * as well as the notation used to represent the operation.
     * @param elist The list of Expressions to exponentiate
     * @param n The Notation to be used to represent the operation
     * @throws IllegalConstruction  If an empty list of expressions if passed as parameter
      * @see #Power(List<Expression>)
      * @see Operation#Operation(List<Expression>,Notation)
     */
    public Power(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "**";
        neutral = 1;
    }

    /**
     * The actual computation of the (binary) arithmetic power of two integers
     * @param l The first integer
     * @param r The second integer that should be the exponent of the first
     * @return The integer that is the result of the exponentiation
     */
    @Override
    public Value op(Value l, Value r) {
        return l.pow(r);
    }
    
    /**
     * Returns the precedence of the power operation
     * @return The precedence of the power operation
     */
    @Override
    public int getPrecedence() {
        return 3;
    }
}

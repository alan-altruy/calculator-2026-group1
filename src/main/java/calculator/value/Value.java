package calculator.value;

/**
 * Represents an arithmetic value in the calculator.
 * Concrete implementations include IntegerValue and RationalValue.
 *
 * @see IntegerValue
 * @see RationalValue
 */
public interface Value {

    /**
     * Add another value to this value.
     * @param other the value to add
     * @return the result of the addition
     */
    Value add(Value other);

    /**
     * Subtract another value from this value.
     * @param other the value to subtract
     * @return the result of the subtraction
     */
    Value sub(Value other);

    /**
     * Multiply this value by another value.
     * @param other the value to multiply by
     * @return the result of the multiplication
     */
    Value mul(Value other);

    /**
     * Divide this value by another value.
     * @param other the value to divide by
     * @return the result of the division
     * @throws ArithmeticException if dividing by zero
     */
    Value div(Value other);

    /**
     * Raise this value to the power of another value.
     * @param other the exponent
     * @return the result of the exponentiation
     */
    Value pow(Value other);

    /**
     * Returns the integer approximation of this value.
     * @return the integer approximation
     */
    int intValue();

    // --- New Binary Operations ---
    default Value mod(Value other) { throw new ArithmeticException("mod not supported for " + this.getClass().getSimpleName()); }
    default Value intDiv(Value other) { throw new ArithmeticException("intDiv not supported for " + this.getClass().getSimpleName()); }

    // --- New Unary Operations & Functions ---
    default Value ln() { throw new ArithmeticException("ln not supported for " + this.getClass().getSimpleName()); }
    default Value log() { throw new ArithmeticException("log not supported for " + this.getClass().getSimpleName()); }
    default Value sin() { throw new ArithmeticException("sin not supported for " + this.getClass().getSimpleName()); }
    default Value cos() { throw new ArithmeticException("cos not supported for " + this.getClass().getSimpleName()); }
    default Value tan() { throw new ArithmeticException("tan not supported for " + this.getClass().getSimpleName()); }
    default Value sinh() { throw new ArithmeticException("sinh not supported for " + this.getClass().getSimpleName()); }
    default Value cosh() { throw new ArithmeticException("cosh not supported for " + this.getClass().getSimpleName()); }
    default Value tanh() { throw new ArithmeticException("tanh not supported for " + this.getClass().getSimpleName()); }
    default Value arcsin() { throw new ArithmeticException("arcsin not supported for " + this.getClass().getSimpleName()); }
    default Value arccos() { throw new ArithmeticException("arccos not supported for " + this.getClass().getSimpleName()); }
    default Value arctan() { throw new ArithmeticException("arctan not supported for " + this.getClass().getSimpleName()); }
    default Value abs() { throw new ArithmeticException("abs not supported for " + this.getClass().getSimpleName()); }
    default Value fact() { throw new ArithmeticException("factorial not supported for " + this.getClass().getSimpleName()); }
}

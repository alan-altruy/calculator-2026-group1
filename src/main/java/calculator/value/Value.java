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
     * For integers, this is the exact value.
     * For rationals, this is the truncated integer division.
     *
     * @return the integer approximation
     */
    int intValue();
}

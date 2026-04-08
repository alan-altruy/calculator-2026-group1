package calculator;

import calculator.operations.Operation;
import calculator.value.IntegerValue;
import calculator.value.Value;
import visitor.Visitor;

/**
 * MyNumber is a concrete class that represents arithmetic numbers,
 * which are a special kind of Expressions, just like operations are.
 *
 * @see Expression
 * @see Operation
 */
public class MyNumber implements Expression
{
  private final Value valueObj;

    /** getter method to obtain the integer value contained in the object
     * (backward compatible, truncates for non-integer domains)
     *
     * @return The integer number contained in the object
     */
  public Integer getValue() { return valueObj.intValue(); }

    /** getter method to obtain the Value object
     *
     * @return The Value object contained in the object
     */
  public Value getValueObject() { return valueObj; }

    /**
     * Constructor method for integer values
     *
     * @param v The integer value to be contained in the object
     */
    public /*constructor*/ MyNumber(int v) {
	  valueObj = new IntegerValue(v);
	  }

    /**
     * Constructor method for any Value type
     *
     * @param v The Value to be contained in the object
     */
    public /*constructor*/ MyNumber(Value v) {
      valueObj = v;
    }

    /**
     * accept method to implement the visitor design pattern to traverse arithmetic expressions.
     * Each number will pass itself to the visitor object to get processed by the visitor.
     *
     * @param v	The visitor object
     */
  public void accept(Visitor v) {
      v.visit(this);
  }

    /**
     * Convert a number into a String to allow it to be printed.
     *
     * @return	The String that is the result of the conversion.
     */
  @Override
  public String toString() {
	  return valueObj.toString();
  }

  /** Two MyNumber expressions are equal if the values they contain are equal
   *
   * @param o The object to compare to
   * @return  A boolean representing the result of the equality test
   */
  @Override
  public boolean equals(Object o) {
      if (o == null) return false;
      if (o == this) return true;
      if (!(o instanceof MyNumber)) return false;
      return this.valueObj.equals(((MyNumber)o).valueObj);
  }

    /** The method hashCode needs to be overridden it the equals method is overridden;
     * 	otherwise there may be problems when you use your object in hashed collections
     * 	such as HashMap, HashSet, LinkedHashSet.
     *
     * @return	The result of computing the hash.
     */
  @Override
  public int hashCode() {
		return valueObj.hashCode();
  }

  @Override
  public int getPrecedence() {
	  return 5;
  }

}

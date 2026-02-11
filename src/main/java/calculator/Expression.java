package calculator;

import visitor.Visitor;

/**
 * Expression is an abstract class that represents arithmetic expressions.
 * It has two concrete subclasses Operation and MyNumber.
 *
 * @see Operation
 * @see MyNumber
 */
public interface Expression {

   /**
    * accept is a method needed to implement the visitor design pattern
    *
    * @param v The visitor object being passed as a parameter
    */
   void accept(Visitor v);

   /**
    * Counts the depth of nested expressions in an arithmetic expression
    *
    * @return The depth of an arithmetic expression
    */
   int countDepth();

   /**
    * Counts the number of operations recursively contained in an arithmetic expression
    *
    * @return The number of operations contained in an arithmetic expression
    */
   int countOps();

   /**
    * Counts the number of values recursively contained in an arithmetic expression
    *
    * @return The number of values contained in an arithmetic expression
    */
   int countNbs();
}

package calculator.enums;

/**
 * Enumeration of the 3 ways to represent an arithmetic expression as a String:
 */
public enum Notation { 
  /**
   * Prefix notation, e.g. "+(1,2)" or "+ 1 2"
   */
  PREFIX, 
  
  /**
   * Infix notation, e.g. "1+2"
   */
  INFIX,
  
  /**
   * Postfix notation, e.g. "(1,2)+" or "1 2 +"
   */
  POSTFIX,

  /**
   * Default notation, which can be set to any of the above notations.
   * This is useful for testing purposes, to check that the default notation 
   * is correctly set and used by the toString() method of operations.
   */
  DEFAULT
  }

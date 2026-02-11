package calculator;

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
  POSTFIX
  }

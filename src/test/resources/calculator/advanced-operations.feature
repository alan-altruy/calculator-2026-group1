Feature: Advanced Operations
    This feature tests the advanced operations of the calculator,
    including exponentiation, logarithms, and trigonometric functions.

    Background:
        Given I initialise a calculator

    Scenario Outline: Exponentiation
        Given I set the current domain to <domain>
        When I provide the input <input>
        Then the output is <output>

        Examples:
            | domain    | input          | output     |
            | "REAL"    | "2 ** 3"       | "8"        |
            | "REAL"    | "5 ** 0"       | "1"        |
            | "REAL"    | "9 ** (1/2)"   | "3"        |
            | "REAL"    | "2 ** -2"      | "0.25"     |
    
    Scenario Outline: Logarithms
        Given I set the current domain to "REAL"
        When I provide the input <input>
        Then the output is <output>

        Examples:
            | input          | output     |
            | "log(10)"      | "1"        |
            | "ln(e)"        | "1"        |
            | "log(1)"       | "0"        |
            | "ln(1)"        | "0"        |
            | "ln(1/e)"      | "-1"       |
            | "log(1/10)"    | "-1"       |

    Scenario Outline: Trigonometric Functions
        Given I set the current domain to "REAL"
        Given I set the angle unit to "RAD"
        When I provide the input <input>
        Then the output is <output>

        Examples:
            | input          | output                |
            | "sin(0)"       | "0"                   |
            | "cos(0)"       | "1"                   |
            | "tan(0)"       | "0"                   |
            | "sin(pi/2)"    | "1"                   |
            | "cos(pi/2)"    | "0"                   |
            | "tan(pi/4)"    | "1"                   |
            | "arcsin(1)"    | "1.5707963267948966"  |
            | "arccos(0)"    | "1.5707963267948966"  |
            | "arctan(1)"    | "0.7853981633974483"  |

    Scenario: Advanced trigonometric functions
        Given I set the current domain to "REAL"
        Given I set the angle unit to "RAD"
        When I provide the input <input>
        Then the output is <output>

        Examples:
            | input          | output                |
            | "sinh(0)"      | "0"                   |
            | "cosh(0)"      | "1"                   |
            | "tanh(0)"      | "0"                   |
            | "sinh(1)"      | "1.1752011936438014"  |
            | "cosh(1)"      | "1.5430806348152437"  |
            | "tanh(1)"      | "0.7615941559557649"  |

    Scenario: Modulo operation
        Given I set the current domain to "REAL"
        When I provide the input "10 mod 3"
        Then the output is "1"

    Scenario: Factorial operation
        Given I set the current domain to "REAL"
        When I provide the input "5!"
        Then the output is "120"

    Scenario: Absolute value
        Given I set the current domain to "REAL"
        When I provide the input "|-5|"
        Then the output is "5"

    Scenario: Integer division
        Given I set the current domain to "REAL"
        When I provide the input "10 // 3"
        Then the output is "3"

    Scenario: Modulo by zero
        When I provide the input "7 mod 0"
        Then an error is raised with message "Modulo by zero"

    Scenario: Factorial of a negative number
        When I provide the input "(-5)!"
        Then an error is raised with message "Factorial of negative integer"

    Scenario: Factorial of a non-integer
        When I provide the input "5.5!"
        Then an error is raised with message "Rounding necessary"

    Scenario: Logarithm of a non-positive number
        When I provide the input "log(-1)"
        Then an error is raised with message "Logarithm of non-positive number"

    Scenario: Natural logarithm of zero
        When I provide the input "ln(0)"
        Then an error is raised with message "Logarithm of non-positive number"

    Scenario: Trigonometric functions with invalid input
        When I provide the input "arcsin(2)"
        Then an error is raised with message "Infinite or NaN"

    
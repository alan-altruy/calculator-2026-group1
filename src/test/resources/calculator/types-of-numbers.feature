Feature: Support different types of numbers
    This feature provides a range of scenarios corresponding to the
    intended external behaviour of a calculator that supports different types of numbers.
    
    Background:
        Given I initialise a calculator

    Scenario Outline: Adding two integer numbers in the integer domain
        Given the domain is "INTEGER"
        When I provide the input <exp> in the specified domain
        Then the result is <result> in the specified domain

        Examples:
            | exp         | result   |
            | "4 + 5"     | "9"      |
            | "7 - 5"     | "2"      |
            | "7 * 5"     | "35"     |
            | "7 / 5"     | "1"      |

    Scenario Outline: Adding two real numbers in the real domain
        Given the domain is "REAL"
        When I provide the input <exp> in the specified domain
        Then the result is <result> in the specified domain

        Examples:
            | exp         | result |
            | "4.5 + 5.2" | "9.7"  |

    Scenario Outline: Adding two rational numbers in the rational domain
        Given the domain is "RATIONAL"
        When I provide the input <exp> in the specified domain
        Then the result is <result> in the specified domain

        Examples: # Exemples with infix, prefix and postfix notations
            | exp           | result   |
            | "1/2 + 1/3"   | "5/6"    |
            | "1/2 - 1/3"   | "1/6"    |
            | "1/2 * 1/3"   | "1/6"    |
            | "1/2 / 1/3"   | "1/6"    |
            | "2/3 + 1/6"   | "5/6"    |
            | "-(1/2, 1/3)" | "1/6"   |
            | "1/2 + (-1/3)"| "1/6"    |

    Scenario Outline: Adding two complex numbers in the complex domain
        Given the domain is "COMPLEX"
        When I provide the input <exp> in the specified domain
        Then the result is <result> in the specified domain

        Examples:
            | exp                         | result        |
            | "2 + 3 * i + 4 + 5 * i"     | "6 + 8 * i"   |
            | "(2 + 3 * i) * (4 + 5 * i)" | "-7 + 22 * i" |

Feature: Parser for Integer Arithmetic Expressions
    This feature provides a range of scenarios corresponding to the
    intended external behaviour of a parser for arithmetic expressions on integers.
    
    Background:
        Given I initialise a calculator

    Scenario: Adding two integer numbers
        When I provide the input "4 + 5"
        Then the output is "9"
    
    Scenario: Subtracting two integer numbers
        When I provide the input "7 - 5"
        Then the output is "2"
    
    Scenario: Multiplying two integer numbers
        When I provide the input "7 * 5"
        Then the output is "35"
    
    Scenario: Dividing two integer numbers
        When I provide the input "7 / 5"
        Then the output is "1"

    Scenario: Multiple operations with correct precedence
        When I provide the input "7 + 3 * 5"
        Then the output is "22"

    Scenario: Multiple operations with parentheses
        When I provide the input "(7 + 3) * 5"
        Then the output is "50"
    
    Scenario: Operations with negative numbers
        When I provide the input "-7 + 3"
        Then the output is "-4"

    Scenario: Multiple operations with POSTFIX notation
        When I provide the input "(8, 6) +"
        Then the output is "14"

    Scenario: Multiple operations with PREFIX notation
        When I provide the input "+ (8, 6)"
        Then the output is "14"

    Scenario: Multiple operations with PREFIX notation and parentheses
        When I provide the input "+ (8, * (6, 2))"
        Then the output is "20"

    Scenario: Multiple operations with POSTFIX notation and parentheses
        When I provide the input "(8, (6, 2) *) +"
        Then the output is "20"
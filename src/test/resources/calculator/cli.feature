Feature: Command Line Interface for Calculator
    This feature provides a range of scenarios corresponding to the
    intended external behaviour of a calculator that supports a command line interface,
    principally for help/exit commands and basic arithmetic operations, others operations are tested in other features.
    
    Background:
        Given I initialise the cli

    Scenario: Help command
        When I provide the input "help" in the cli
        Then I should see the help message

    Scenario Outline: Making a calculation in the cli
        When I provide the input <exp> in the cli
        Then I should see the result <result> in the cli

        Examples:
            | exp         | result |
            | "4 + 5"     | "9"    |
            | "7 - 5"     | "2"    |
            | "7 * 5"     | "35"   |

    Scenario Outline: Test input with switch domain in the cli
        Given I switch to the domain <domain> in the cli
        And I provide the input <exp> in the cli
        Then I should see the result <result> in the cli

        Examples:
            | domain      | exp                     | result      |
            | "REAL"      | "4.5 + 5.2"             | "9.7"       |
            | "INTEGER"   | "4 + 5"                 | "9"         |
            | "RATIONAL"  | "1/2 + 1/3"             | "5/6"       |
            | "COMPLEX"   | "1 + 2 * i + 3 + 4 * i" | "4 + 6 * i" |
    
    Scenario Outline: Switch between domains in the cli
        When I provide the input <exp> in the cli
        Then I should see the message <result> in the cli

        Examples:
            | exp                 | result                               |
            | "domain REAL"       | "Number domain switched to REAL"     |
            | "domain INTEGER"    | "Number domain switched to INTEGER"  |
            | "domain RATIONAL"   | "Number domain switched to RATIONAL" |
            | "domain COMPLEX"    | "Number domain switched to COMPLEX"  |
    
    Scenario Outline: Switch between AngleMode in the cli
        When I provide the input <exp> in the cli
        Then I should see the message <result> in the cli

        Examples:
            | exp            | result                               |
            | "mode DEG"     | "Angle mode switched to DEG"         |
            | "mode RAD"     | "Angle mode switched to RAD"         |

    Scenario Outline: Change the precision in REAL domain in the cli
        When I provide the input <exp> in the cli
        Then I should see the message <result> in the cli

        Examples:
            | exp                 | result                               |
            | "precision 3"       | "Precision set to 3" |
            | "precision 5"       | "Precision set to 5" |

    Scenario Outline: Exit command
        When I provide the input <exit> in the cli
        Then the cli should exit

        Examples:
            | exit   |
            | "exit" |
            | "quit" |
            
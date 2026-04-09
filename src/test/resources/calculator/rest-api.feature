Feature: REST API for Calculator
    This feature provides a range of scenarios corresponding to the
    intended external behaviour of a REST API for a calculator.
    
    Background:
        Given I start the REST API server
    
    Scenario: Evaluating an addition operation
        When I send a POST request to evaluate with the following JSON body "5 + 4"
        Then the response status code is 200
        And the response body is "9"

        # Only one scenario is provided because the API is very simple and the other
        # scenarios would be very similar, but with different input and expected output.
        # The important thing is to test that the API correctly send and receive the expected data,
        # the logic is implemented in the Calculator by the Parser and the Expression classes,
        #which are tested separately in unit tests and in the other feature files.
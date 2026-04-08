package calculator.rest;

import calculator.value.Value;

/**
 * A response object for the evaluate endpoint.
 */
public class EvaluateResponse {
    /**
     * The result of evaluating the expression, if successful. Null if there was an error.
     */
    private String result;
    /**
     * An error message if the evaluation failed. Null if the evaluation was successful.
     */
    private String error;

    /**
     * Constructs an empty response object.
     */
    public EvaluateResponse() {}

/*    *//**
     * Constructs a response object with a successful evaluation result.
     * @param result the result of the evaluation
     *//*
    public EvaluateResponse(String result) { this.result = result; }

    *//**
     * Constructs a response object with an error message.
     * @param error the error message
     *//*
    public EvaluateResponse(String error) { this.error = error; }*/

    public EvaluateResponse(String result, String error) {
        this.result = result;
        this.error = error;
    }

    /**
     * Returns the result of the evaluation, or null if there was an error.
     * @return the result of the evaluation, or null if there was an error
     */
    public String getResult() { return result; }

    /**
     * Sets the result of the evaluation. Should only be used if the evaluation was successful.
     * @param result the result of the evaluation
     */
    public void setResult(String result) { this.result = result; }

    /**
     * Returns the error message if the evaluation failed, or null if the evaluation was successful.
     * @return the error message if the evaluation failed, or null if the evaluation was successful
     */
    public String getError() { return error; }

    /**
     * Sets the error message. Should only be used if the evaluation failed.
     * @param error the error message
     */
    public void setError(String error) { this.error = error; }
}

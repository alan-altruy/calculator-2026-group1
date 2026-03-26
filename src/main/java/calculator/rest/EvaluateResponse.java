package calculator.rest;

public class EvaluateResponse {
    private Integer result;
    private String error;

    public EvaluateResponse() {}

    public EvaluateResponse(Integer result) { this.result = result; }

    public EvaluateResponse(String error) { this.error = error; }

    public Integer getResult() { return result; }

    public void setResult(Integer result) { this.result = result; }

    public String getError() { return error; }

    public void setError(String error) { this.error = error; }
}

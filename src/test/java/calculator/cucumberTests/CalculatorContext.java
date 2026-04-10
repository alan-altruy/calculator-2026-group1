package calculator.cucumberTests;

import calculator.Calculator;

public class CalculatorContext {
    private Calculator calculator;

    public Calculator getCalculator() {
        return calculator;
    }

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }
}
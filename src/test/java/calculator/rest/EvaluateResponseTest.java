package calculator.rest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluateResponseTest {

    @Test
    void testEvaluateResponseDefaultConstructorAndSetters() {
        calculator.rest.EvaluateResponse r = new calculator.rest.EvaluateResponse();
        // default constructor -> null fields
        assertThat(r.getResult()).isNull();
        assertThat(r.getError()).isNull();

        r.setResult(123);
        assertThat(r.getResult()).isEqualTo(123);

        r.setError("boom");
        assertThat(r.getError()).isEqualTo("boom");
    }

}

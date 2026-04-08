package calculator.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluateResponseTest {

    /*@Test
    void testEvaluateResponseDefaultConstructorAndSetters() {
        calculator.rest.EvaluateResponse r = new calculator.rest.EvaluateResponse();
        // default constructor -> null fields
        assertThat(r.getResult()).isNull();
        assertThat(r.getError()).isNull();

        r.setResult(123);
        assertThat(r.getResult()).isEqualTo(123);

        r.setError("boom");
        assertThat(r.getError()).isEqualTo("boom");
    }*/
    @Test
    void constructorWithError_setsErrorAndNullResult() {
        EvaluateResponse resp = new EvaluateResponse("erreur de test");
        assertEquals("erreur de test", resp.getError());
        assertNull(resp.getResult());
    }
}

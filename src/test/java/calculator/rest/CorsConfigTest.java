package calculator.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CalculatorRestController.class)
@org.springframework.context.annotation.Import(CorsConfig.class)
public class CorsConfigTest {

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @Test
    void corsAllowsAnyOrigin() throws Exception {
        String body = "{\"ast\":{\"type\":\"number\",\"value\":1}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .header("Origin", "http://example.com")
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        assertThat(res.getResponse().getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
    }
}

package calculator.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CalculatorRestController.class)
@org.springframework.context.annotation.Import(RestExceptionHandler.class)
public class RestExceptionHandlerTest {

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @Test
    void handlerReturnsExpectedErrorBody() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"foo\",\"args\":[]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        assertThat(res.getResponse().getContentAsString()).contains("Illegal construction of expression");
    }

    @Test
    void malformedJsonTriggersBadRequestHandler() throws Exception {
        String badJson = "{"; // malformed JSON
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(badJson))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        assertThat(res.getResponse().getContentAsString()).contains("Malformed JSON request");
    }

    @Test
    void divisionByZeroHandledAsInternalServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"/\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":0}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(500);
        assertThat(res.getResponse().getContentAsString()).contains("Internal server error");
    }

    @Test
    void argsNotArrayHandledAsInternalServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":{\"type\":\"number\",\"value\":1}}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(500);
        assertThat(res.getResponse().getContentAsString()).contains("Internal server error");
    }
}

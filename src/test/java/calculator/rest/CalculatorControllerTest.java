package calculator.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;



@WebMvcTest(CalculatorRestController.class)
@org.springframework.context.annotation.Import({RestExceptionHandler.class, CorsConfig.class})
public class CalculatorControllerTest {

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @Test
    void responseHasJsonContentType() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":4},{\"type\":\"number\",\"value\":5}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON));
    }

    @Test
    void testEvaluateBadRequest() throws Exception {
        String badBody = "{\"no_ast\":true}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(badBody))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUnsupportedMediaTypeReturns415() throws Exception {
        String body = "not json";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isUnsupportedMediaType());
    }

    @Test
    void testInvalidNotationIsIgnoredAndStillEvaluates() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"notation\":\"weird\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.result").value(5));
    }

    @Test
    void testValidNotationIsAccepted() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"notation\":\"prefix\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.result").value(5));
    }

    @Test
    void testDivisionByZeroProducesServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"/\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":0}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100).isEqualTo(5);
    }

    @Test
    void testUnknownOpReturnsServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"foo\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":2}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testMissingOpReturnsServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"args\":[{\"type\":\"number\",\"value\":1}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testArgsContainingNullProducesServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[null,{\"type\":\"number\",\"value\":1}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testArgsNotArrayProducesServerError() throws Exception {
        // args provided but not an array
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":{\"type\":\"number\",\"value\":1}}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100).isEqualTo(5);
    }

    @Test
    void testMissingArgsProducesServerError() throws Exception {
        // operation with no 'args' field
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\"}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testMinusOperationEvaluates() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"-\",\"args\":[{\"type\":\"number\",\"value\":5},{\"type\":\"number\",\"value\":2}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.result").value(3));
    }

    @Test
    void testEvaluateSuccess() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"operation\",\"op\":\"*\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.result").value(7));
    }
}

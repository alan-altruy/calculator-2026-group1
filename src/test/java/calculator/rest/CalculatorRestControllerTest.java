package calculator.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import calculator.IllegalConstruction;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@org.springframework.context.annotation.Import({RestExceptionHandler.class, CorsConfig.class})
public class CalculatorRestControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private org.springframework.test.web.servlet.MockMvc mockMvc;

    private static final String API_URL = "/api/v1/evaluate";
    private static final String RESULT = "$.result";

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void responseHasJsonContentType() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":4},{\"type\":\"number\",\"value\":5}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON));
    }

    @Test
    void testEvaluateBadRequest() throws Exception {
        String badBody = "{\"no_ast\":true}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(badBody))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUnsupportedMediaTypeReturns415() throws Exception {
        String body = "not json";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isUnsupportedMediaType());
    }

    @Test
    void testInvalidNotationIsIgnoredAndStillEvaluates() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"notation\":\"weird\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(5));
    }

    @Test
    void testValidNotationIsAccepted() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"notation\":\"prefix\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(5));
    }

    @Test
    void testDivisionByZeroProducesServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"/\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":0}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100).isEqualTo(5);
    }

    @Test
    void testUnknownOpReturnsServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"foo\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":2}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testMissingOpReturnsServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"args\":[{\"type\":\"number\",\"value\":1}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testArgsContainingNullProducesServerError() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[null,{\"type\":\"number\",\"value\":1}]}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
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
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
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
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 4 || status / 100 == 5).isTrue();
    }

    @Test
    void testMinusOperationEvaluates() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"-\",\"args\":[{\"type\":\"number\",\"value\":5},{\"type\":\"number\",\"value\":2}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(3));
    }

    @Test
    void testEvaluateSuccess() throws Exception {
        String body = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"operation\",\"op\":\"*\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}]}}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(7));
    }
    
    @Test
    void toExpressionNullThrowsIllegalConstruction_viaMethodHandle() throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));

        assertThrows(IllegalConstruction.class, () -> mh.invoke(controller, (JsonNode) null));
    }

    @Test
    void parseNotationNullAndMissingReturnInfix_viaMethodHandle() throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "parseNotation",
                MethodType.methodType(calculator.Notation.class, com.fasterxml.jackson.databind.JsonNode.class));

        // null node -> default to INFIX
        Object resNull = mh.invoke(controller, (com.fasterxml.jackson.databind.JsonNode) null);
        assertEquals(calculator.Notation.INFIX, resNull);

        // node without 'notation' field -> default to INFIX
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode noNotation = mapper.readTree("{\"type\":\"operation\"}");
        Object resNoField = mh.invoke(controller, noNotation);
        assertEquals(calculator.Notation.INFIX, resNoField);
    }

    @Test
    void parseNotationInvalidValueReturnsInfix_viaMethodHandle() throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "parseNotation",
                MethodType.methodType(calculator.Notation.class, com.fasterxml.jackson.databind.JsonNode.class));

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode invalid = mapper.readTree("{\"notation\":\"weird\"}");
        Object resInvalid = mh.invoke(controller, invalid);
        assertEquals(calculator.Notation.INFIX, resInvalid);
    }
}

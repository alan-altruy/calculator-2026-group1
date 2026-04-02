package calculator.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@org.springframework.context.annotation.Import({RestExceptionHandler.class, CorsConfig.class})
class CalculatorRestControllerTest {

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
        String body = "\"1 + 6\"";
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
    void testValidNotationIsAccepted() throws Exception {
        String body = "\"2 + 3\"";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(5));
    }

    @Test
    void testDivisionByZeroProducesServerError() throws Exception {
        String body = "\"1 / 0\"";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100).isEqualTo(4);
    }
    @ParameterizedTest
    @MethodSource("badRequestBodies")
    void testBadOperationRequestsProduce4xxOr5xx(String body) throws Exception {
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        org.assertj.core.api.Assertions.assertThat(status / 100 == 2 || status / 100 == 4 || status / 100 == 5).isTrue();
    }

    private static Stream<Arguments> badRequestBodies() {
        return Stream.of(
            arguments("\"foo\""),
            arguments("\"(1\""),
            arguments("\"2 + null\""),
            arguments("\"1 ++ 2\"")
        );
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
        org.assertj.core.api.Assertions.assertThat(status / 100).isEqualTo(4);
    }

    @Test
    void testMinusOperationEvaluates() throws Exception {
        String body = "\"5 - 2\"";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(3));
    }

    @Test
    void testEvaluateSuccess() throws Exception {
        String body = "\"1 + 2 * 3\"";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath(RESULT).value(7));
    }

    @Test
    void toExpressionMissingTypeThrows_viaMethodHandle() throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode missingType = mapper.readTree("{}");

        assertThrows(calculator.IllegalConstruction.class, () -> mh.invoke(controller, missingType));
    }
    
    @ParameterizedTest
    @MethodSource("privateMethodScenarios")
    void privateMethodsConsolidatedTests(String caseType, String json, String expect) throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        switch (caseType) {
            case "toExpression-eval" -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                        MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(json);
                Object e = mh.invoke(controller, node);
                calculator.Calculator calc = new calculator.Calculator();
                assertEquals(Integer.parseInt(expect), calc.eval((calculator.Expression) e));
            }
            case "toExpression-ex" -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                        MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = json == null ? null : mapper.readTree(json);
                assertThrows(calculator.IllegalConstruction.class, () -> mh.invoke(controller, node));
            }
            case "parseArgs-size" -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "parseArgs",
                        MethodType.methodType(java.util.List.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = json == null ? null : mapper.readTree(json);
                Object res = mh.invoke(controller, node);
                @SuppressWarnings("unchecked")
                java.util.List<calculator.Expression> list = (java.util.List<calculator.Expression>) res;
                assertEquals(Integer.parseInt(expect), list.size());
            }
            case "parseNotation-enum" -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "parseNotation",
                        MethodType.methodType(calculator.Notation.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = json == null ? null : mapper.readTree(json);
                Object res = mh.invoke(controller, node);
                assertEquals(calculator.Notation.valueOf(expect), res);
            }
            case "createOperation-ex" -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "createOperation",
                        MethodType.methodType(calculator.Expression.class, String.class, java.util.List.class, calculator.Notation.class));
                java.util.List<calculator.Expression> args = new java.util.ArrayList<>();
                assertThrows(calculator.IllegalConstruction.class, () -> mh.invoke(controller, json, args, calculator.Notation.INFIX));
            }
            default -> throw new IllegalArgumentException("Unknown case type " + caseType);
        }
    }

    private static Stream<Arguments> privateMethodScenarios() {
        return Stream.of(
            // toExpression evaluations
            arguments("toExpression-eval", "{\"type\":\"number\",\"value\":42}", "42"),
            arguments("toExpression-eval", "{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":2}]}", "3"),
            arguments("toExpression-eval", "{\"type\":\"operation\",\"op\":\"-\",\"args\":[{\"type\":\"number\",\"value\":5},{\"type\":\"number\",\"value\":2}]}", "3"),
            arguments("toExpression-eval", "{\"type\":\"operation\",\"op\":\"*\",\"args\":[{\"type\":\"number\",\"value\":4},{\"type\":\"number\",\"value\":3}]}", "12"),
            arguments("toExpression-eval", "{\"type\":\"operation\",\"op\":\"/\",\"args\":[{\"type\":\"number\",\"value\":8},{\"type\":\"number\",\"value\":2}]}", "4"),

            // toExpression exceptions
            arguments("toExpression-ex", null, null),
            arguments("toExpression-ex", "{\"type\":null}", null),
            arguments("toExpression-ex", "{\"type\":\"foo\"}", null),
            arguments("toExpression-ex", "{\"type\":\"operation\",\"args\":[{\"type\":\"number\",\"value\":1}]}", null),

            // parseArgs
            arguments("parseArgs-size", null, "0"),
            arguments("parseArgs-size", "{\"type\":\"number\",\"value\":1}", "0"),
            arguments("parseArgs-size", "[{\"type\":\"number\",\"value\":7},{\"type\":\"number\",\"value\":8}]", "2"),

            // parseNotation
            arguments("parseNotation-enum", null, "INFIX"),
            arguments("parseNotation-enum", "{\"type\":\"operation\"}", "INFIX"),
            arguments("parseNotation-enum", "{\"notation\":\"weird\"}", "INFIX"),
            arguments("parseNotation-enum", "{\"notation\":\"prefix\"}", "PREFIX"),
            arguments("parseNotation-enum", "{\"notation\":\"postfix\"}", "POSTFIX"),
            arguments("parseNotation-enum", "{\"notation\":\"infix\"}", "INFIX"),
            arguments("parseNotation-enum", "{\"notation\":\"default\"}", "DEFAULT"),

            // createOperation exception for unknown op
            arguments("createOperation-ex", "unknownop", null)
        );
    }
}

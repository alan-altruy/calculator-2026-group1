package calculator.rest;

import calculator.enums.Notation;
import calculator.exceptions.IllegalConstruction;
import org.junit.jupiter.api.Test;
import calculator.Main;
import calculator.enums.NumberDomain;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Map;

@SpringBootTest
@org.springframework.context.annotation.Import({RestExceptionHandler.class, CorsConfig.class})
class CalculatorRestControllerTest {

    private static final String CASE_TOEXPR_EVAL = "toExpression-eval";
    private static final String CASE_TOEXPR_EX = "toExpression-ex";
    private static final String CASE_PARSEARGS_SIZE = "parseArgs-size";
    private static final String CASE_PARSENOTATION_ENUM = "parseNotation-enum";
    private static final String NOTATION_INFIX = "INFIX";
    private static final String DOM_STRING = "domain";

    @Test
    void toExpressionMissingTypeThrows_viaMethodHandle() throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode missingType = mapper.readTree("{}");

        assertThrows(IllegalConstruction.class, () -> mh.invoke(controller, missingType));
    }


    @Test
    void computeDivisionByZeroThrowsArithmeticException() {
        CalculatorRestController controller = new CalculatorRestController();
        ResponseEntity<EvaluateResponse> response = controller.compute("4/0");
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("privateMethodScenarios")
    void privateMethodsConsolidatedTests(String caseType, String json, String expect) throws Throwable {
        CalculatorRestController controller = new CalculatorRestController();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CalculatorRestController.class, MethodHandles.lookup());
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        switch (caseType) {
            case CASE_TOEXPR_EVAL -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                        MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(json);
                Object e = mh.invoke(controller, node);
                calculator.Calculator calc = new calculator.Calculator();
                assertEquals(Integer.parseInt(expect), calc.eval((calculator.Expression) e));
            }
            case CASE_TOEXPR_EX -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "toExpression",
                        MethodType.methodType(calculator.Expression.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = json == null ? null : mapper.readTree(json);
                assertThrows(IllegalConstruction.class, () -> mh.invoke(controller, node));
            }
            case CASE_PARSEARGS_SIZE -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "parseArgs",
                        MethodType.methodType(java.util.List.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = json == null ? null : mapper.readTree(json);
                Object res = mh.invoke(controller, node);
                @SuppressWarnings("unchecked")
                java.util.List<calculator.Expression> list = (java.util.List<calculator.Expression>) res;
                assertEquals(Integer.parseInt(expect), list.size());
            }
            case CASE_PARSENOTATION_ENUM -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "parseNotation",
                        MethodType.methodType(Notation.class, com.fasterxml.jackson.databind.JsonNode.class));
                com.fasterxml.jackson.databind.JsonNode node = json == null ? null : mapper.readTree(json);
                Object res = mh.invoke(controller, node);
                assertEquals(Notation.valueOf(expect), res);
            }
            case "createOperation-ex" -> {
                MethodHandle mh = lookup.findVirtual(CalculatorRestController.class, "createOperation",
                        MethodType.methodType(calculator.Expression.class, String.class, java.util.List.class, Notation.class));
                java.util.List<calculator.Expression> args = new java.util.ArrayList<>();
                assertThrows(IllegalConstruction.class, () -> mh.invoke(controller, json, args, Notation.INFIX));
            }
            default -> throw new IllegalArgumentException("Unknown case type " + caseType);
        }
    }

    private static Stream<Arguments> privateMethodScenarios() {
        return Stream.of(
            // toExpression evaluations

            arguments(CASE_TOEXPR_EVAL, "{\"type\":\"number\",\"value\":42}", "42"),
            arguments(CASE_TOEXPR_EVAL, "{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":2}]}", "3"),
            arguments(CASE_TOEXPR_EVAL, "{\"type\":\"operation\",\"op\":\"-\",\"args\":[{\"type\":\"number\",\"value\":5},{\"type\":\"number\",\"value\":2}]}", "3"),
            arguments(CASE_TOEXPR_EVAL, "{\"type\":\"operation\",\"op\":\"*\",\"args\":[{\"type\":\"number\",\"value\":4},{\"type\":\"number\",\"value\":3}]}", "12"),
            arguments(CASE_TOEXPR_EVAL, "{\"type\":\"operation\",\"op\":\"/\",\"args\":[{\"type\":\"number\",\"value\":8},{\"type\":\"number\",\"value\":2}]}", "4"),

            // toExpression exceptions
            arguments(CASE_TOEXPR_EX, null, null),
            arguments(CASE_TOEXPR_EX, "{\"type\":null}", null),
            arguments(CASE_TOEXPR_EX, "{\"type\":\"foo\"}", null),
            arguments(CASE_TOEXPR_EX, "{\"type\":\"operation\",\"args\":[{\"type\":\"number\",\"value\":1}]}", null),

            // parseArgs
            arguments(CASE_PARSEARGS_SIZE, null, "0"),
            arguments(CASE_PARSEARGS_SIZE, "{\"type\":\"number\",\"value\":1}", "0"),
            arguments(CASE_PARSEARGS_SIZE, "[{\"type\":\"number\",\"value\":7},{\"type\":\"number\",\"value\":8}]", "2"),

            // parseNotation
            arguments(CASE_PARSENOTATION_ENUM, null, NOTATION_INFIX),
            arguments(CASE_PARSENOTATION_ENUM, "{\"type\":\"operation\"}", NOTATION_INFIX),
            arguments(CASE_PARSENOTATION_ENUM, "{\"notation\":\"weird\"}", NOTATION_INFIX),
            arguments(CASE_PARSENOTATION_ENUM, "{\"notation\":\"prefix\"}", "PREFIX"),
            arguments(CASE_PARSENOTATION_ENUM, "{\"notation\":\"postfix\"}", "POSTFIX"),
            arguments(CASE_PARSENOTATION_ENUM, "{\"notation\":\"infix\"}", NOTATION_INFIX),
            arguments(CASE_PARSENOTATION_ENUM, "{\"notation\":\"default\"}", "DEFAULT"),

            // createOperation exception for unknown op
            arguments("createOperation-ex", "unknownop", null)
        );
    }

    @Test
    void switchDomainSetsMainCurrentDomain() {
        CalculatorRestController controller = new CalculatorRestController();

        controller.switchDomain(Map.of(DOM_STRING, "REAL"));
        assertEquals(NumberDomain.REAL, Main.getCurrentDomain());

        controller.switchDomain(Map.of(DOM_STRING, "COMPLEX"));
        assertEquals(NumberDomain.COMPLEX, Main.getCurrentDomain());

        controller.switchDomain(Map.of(DOM_STRING, "RATIONAL"));
        assertEquals(NumberDomain.RATIONAL, Main.getCurrentDomain());

        // default case -> INTEGER
        controller.switchDomain(Map.of(DOM_STRING, "UNKNOWN"));
        assertEquals(NumberDomain.INTEGER, Main.getCurrentDomain());
    }
}

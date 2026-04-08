package calculator.rest;

import calculator.value.Value;
import com.fasterxml.jackson.databind.JsonNode;

import calculator.Calculator;
import calculator.operations.Divides;
import calculator.Expression;
import calculator.ExpressionParser;
import calculator.exceptions.IllegalConstruction;
import calculator.operations.Minus;
import calculator.MyNumber;
import calculator.enums.Notation;
import calculator.operations.Plus;
import calculator.operations.Times;
import calculator.enums.NumberDomain;
import calculator.Main;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A REST controller for evaluating calculator expressions represented as ASTs in JSON format.
 * The endpoint accepts a POST request with a JSON body containing an "ast" field that represents
 * the expression to evaluate. The AST should follow a specific structure where each node has a "type"
 */
@RestController
@RequestMapping("/api/v1")
public class CalculatorRestController {

    /**
     * The type identifier for a number node in the AST.
     */
    private static final String TYPE_NUMBER = "number";
    /**
     * The type identifier for an operation node in the AST.
     */
    private static final String TYPE_OPERATION = "operation";


    @Operation(
            summary = "Evaluate an expression AST",
            description = """
                    Accepts an AST JSON and returns the evaluated integer result.
                    
                    Example fetch call:
                    
                    ´´´javascript
                    fetch('http://localhost:8080/api/v1/evaluate', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ ast: { type: 'operation', op: '+', args: [ { type: 'number', value: 1 }, { type: 'number', value: 6 } ] } })
                    })
                        .then(r => r.json())
                        .then(j => console.log('result:', j.result));
                    ´´´
                    
                    From a Swing app you can either call `Calculator.eval()` locally or use Java `HttpClient` to POST the same AST to this endpoint.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "evaluation successful",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"result\":7}"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
                    @ApiResponse(responseCode = "418", description = "I'm a teapot..."),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"operation\",\"op\":\"*\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}]}}")))
    @PostMapping(value = "/evaluate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluateResponse> compute(@RequestBody String input) {
        input = input.replace("\"", "");
        Expression e = ExpressionParser.parse(input);
        Calculator c = new Calculator();
        try {
            Value result = c.evalValue(e);
            return ResponseEntity.ok(new EvaluateResponse(result.toString(),null));
        } catch (ArithmeticException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Converts a JsonNode representing an AST into an Expression object.
     *
     * @param node the JsonNode to convert
     * @return the corresponding Expression object
     * @throws IllegalConstruction if the AST is malformed or contains unknown types/operations
     */
    private Expression toExpression(JsonNode node) throws IllegalConstruction {
        if (node == null) throw new IllegalConstruction();
        String type = node.has("type") ? node.get("type").asText() : null;
        if (TYPE_NUMBER.equalsIgnoreCase(type)) {
            int v = node.get("value").asInt();
            return new MyNumber(v);
        } else if (TYPE_OPERATION.equalsIgnoreCase(type)) {
            String op = node.has("op") ? node.get("op").asText() : null;
            List<Expression> args = parseArgs(node.get("args"));
            Notation notation = parseNotation(node);
            if (op == null) throw new IllegalConstruction();
            return createOperation(op, args, notation);
        } else {
            throw new IllegalConstruction();
        }
    }

    private List<Expression> parseArgs(JsonNode arr) throws IllegalConstruction {
        List<Expression> args = new ArrayList<>();
        if (arr == null || !arr.isArray()) return args;
        for (JsonNode n : arr) {
            args.add(toExpression(n));
        }
        return args;
    }

    private Notation parseNotation(JsonNode node) {
        if (node == null || !node.has("notation")) return Notation.INFIX;
        try {
            return Notation.valueOf(node.get("notation").asText().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return Notation.INFIX;
        }
    }

    private Expression createOperation(String op, List<Expression> args, Notation notation) throws IllegalConstruction {
        String k = op.toLowerCase(Locale.ROOT);
        return switch (k) {
            case "plus", "+" -> new Plus(args, notation);
            case "minus", "-" -> new Minus(args, notation);
            case "times", "*" -> new Times(args, notation);
            case "divides", "/" -> new Divides(args, notation);
            default -> throw new IllegalConstruction();
        };
    }

    @PostMapping("/switchDomain")
    public void switchDomain(@RequestBody Map<String, String> body) {
        String domain = body.get("domain");
        switch (domain) {
            case "REAL" -> Main.setCurrentDomain(NumberDomain.REAL);
            case "COMPLEX" -> Main.setCurrentDomain(NumberDomain.COMPLEX);
            case "RATIONAL" -> Main.setCurrentDomain(NumberDomain.RATIONAL);
            default -> Main.setCurrentDomain(NumberDomain.INTEGER);
        }
    }
}

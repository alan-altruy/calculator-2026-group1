package calculator.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import calculator.*;
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
            description = "Accepts an AST JSON and returns the evaluated integer result.\n\n" +
                "Example fetch call:\n\n```javascript\nfetch('http://localhost:8080/api/v1/evaluate', {\n  method: 'POST',\n  headers: { 'Content-Type': 'application/json' },\n  body: JSON.stringify({ ast: { type: 'operation', op: '+', args: [ { type: 'number', value: 1 }, { type: 'number', value: 6 } ] } })\n})\n  .then(r => r.json())\n  .then(j => console.log('result:', j.result));\n```\n\n" +
                "From a Swing app you can either call `Calculator.eval()` locally or use Java `HttpClient` to POST the same AST to this endpoint.",
            responses = {
                @ApiResponse(responseCode = "200", description = "evaluation successful",
                    content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = "{\"result\":7}"))),
                @ApiResponse(responseCode = "400", description = "Bad Request"),
                @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
        )
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"operation\",\"op\":\"*\",\"args\":[{\"type\":\"number\",\"value\":2},{\"type\":\"number\",\"value\":3}]}]}}"))
        )
        @PostMapping(value = "/evaluate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<EvaluateResponse> evaluate(@RequestBody Object bodyObj) throws IllegalConstruction {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode body = mapper.valueToTree(bodyObj);
        JsonNode ast = body.get("ast");
        if (ast == null) {
            return ResponseEntity.badRequest().body(new EvaluateResponse("missing 'ast' field"));
        }
        Expression expr = toExpression(ast);
        Calculator calc = new Calculator();
        int result = calc.eval(expr);
        return ResponseEntity.ok(new EvaluateResponse(result));
    }

    /**
     * Converts a JsonNode representing an AST into an Expression object.
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
}

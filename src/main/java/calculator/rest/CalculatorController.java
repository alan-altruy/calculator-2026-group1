package calculator.rest;

import com.fasterxml.jackson.databind.JsonNode;
import calculator.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    @PostMapping(value = "/evaluate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluateResponse> evaluate(@RequestBody JsonNode body) throws IllegalConstruction {
        JsonNode ast = body.get("ast");
        if (ast == null) {
            return ResponseEntity.badRequest().body(new EvaluateResponse("missing 'ast' field"));
        }
        Expression expr = toExpression(ast);
        Calculator calc = new Calculator();
        int result = calc.eval(expr);
        return ResponseEntity.ok(new EvaluateResponse(result));
    }

    private Expression toExpression(JsonNode node) throws IllegalConstruction {
        if (node == null) throw new IllegalConstruction();
        String type = node.has("type") ? node.get("type").asText() : null;
        if ("number".equalsIgnoreCase(type)) {
            int v = node.get("value").asInt();
            return new MyNumber(v);
        } else if ("operation".equalsIgnoreCase(type)) {
            String op = node.has("op") ? node.get("op").asText() : null;
            List<Expression> args = new ArrayList<>();
            JsonNode arr = node.get("args");
            if (arr != null && arr.isArray()) {
                for (JsonNode n : arr) {
                    args.add(toExpression(n));
                }
            }
            Notation notation = Notation.INFIX;
            if (node.has("notation")) {
                try {
                    notation = Notation.valueOf(node.get("notation").asText().toUpperCase());
                } catch (IllegalArgumentException ignored) {}
            }
            if (op == null) throw new IllegalConstruction();
            switch (op.toLowerCase()) {
                case "plus", "+" -> {
                    return new Plus(args, notation);
                }
                case "minus", "-" -> {
                    return new Minus(args, notation);
                }
                case "times", "*" -> {
                    return new Times(args, notation);
                }
                case "divides", "/" -> {
                    return new Divides(args, notation);
                }
                default -> throw new IllegalConstruction();
            }
        } else {
            throw new IllegalConstruction();
        }
    }
}

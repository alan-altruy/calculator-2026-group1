package calculator.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import calculator.*;
import visitor.*;

@RestController
@CrossOrigin
public class CalculatorController {

    List<Expression> params = new ArrayList<>();
    Expression e;
    Calculator c = new Calculator();

    @PostMapping("/calculate")
    public Map<String, Integer> calculate(@RequestBody Map<String, String> body) throws IllegalConstruction {

        Collections.addAll(params, new MyNumber(6), new MyNumber(7));
        e = new Times(params, Notation.INFIX);
        int res =c.eval(e);

        return Map.of("result", res);
    }
}

package calculator.rest;

import calculator.IllegalConstruction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalConstruction.class)
    public ResponseEntity<Map<String, String>> handleIllegalConstruction(IllegalConstruction ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Illegal construction of expression"));
    }
}

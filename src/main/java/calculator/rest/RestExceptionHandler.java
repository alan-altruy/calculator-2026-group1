package calculator.rest;

import calculator.IllegalConstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(IllegalConstruction.class)
    public ResponseEntity<Map<String, String>> handleIllegalConstruction(IllegalConstruction ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Illegal construction of expression"));
    }

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedMediaType(org.springframework.web.HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Map.of("error", "Unsupported Media Type"));
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestMessage(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Malformed JSON request"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        log.error("Unhandled exception in controller", ex);
        return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
    }
}

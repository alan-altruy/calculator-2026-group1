package calculator.rest;

import calculator.IllegalConstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

/**
 * A global exception handler for REST API endpoints.
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * The logger for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
    /**
     * The key used in error response bodies to indicate the error message.
     */
    private static final String ERROR_KEY = "error";

    /**
     * Handles IllegalConstruction exceptions thrown by controller methods, returning a 400 Bad Request response
     * with a JSON body containing an error message.
     * @param ex the IllegalConstruction exception that was thrown
     * @return a ResponseEntity with a 400 status code and a JSON body containing an error message
     */
    @ExceptionHandler(IllegalConstruction.class)
    public ResponseEntity<Map<String, String>> handleIllegalConstruction(IllegalConstruction ex) {
        return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, "Illegal construction of expression"));
    }

    /**
     * Handles HttpMediaTypeNotSupportedException exceptions thrown when a client sends a request with an unsupported media type,
     * returning a 415 Unsupported Media Type response with a JSON body containing an error message.
     * @param ex the HttpMediaTypeNotSupportedException exception that was thrown
     * @return a ResponseEntity with a 415 status code and a JSON body containing an error message
     */
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedMediaType(org.springframework.web.HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Map.of(ERROR_KEY, "Unsupported Media Type"));
    }

    /**
     * Handles HttpMessageNotReadableException exceptions thrown when a client sends a request with malformed JSON, returning a 400 Bad Request response
     * with a JSON body containing an error message.
     * @param ex the HttpMessageNotReadableException exception that was thrown
     * @return a ResponseEntity with a 400 status code and a JSON body containing an error message
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestMessage(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, "Malformed JSON request"));
    }

    /**
     * Handles NoResourceFoundException (missing static resources) by returning 404 without error logging.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                .body(Map.of(ERROR_KEY, "Resource not found"));
    }

    /**
     * Handles any other exceptions that were not specifically handled by other methods, returning a 500 Internal Server Error response
     * with a JSON body containing a generic error message. The exception is also logged for debugging purposes.
     * @param ex the exception that was thrown
     * @return a ResponseEntity with a 500 status code and a JSON body containing a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        log.error("Unhandled exception in controller", ex);
        return ResponseEntity.status(500).body(Map.of(ERROR_KEY, "Internal server error"));
    }
}

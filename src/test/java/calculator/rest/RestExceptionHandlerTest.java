package calculator.rest;

import calculator.exceptions.IllegalConstruction;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RestExceptionHandlerTest {

	private final RestExceptionHandler handler = new RestExceptionHandler();

	private static final String ERROR_KEY = "error";

	@Test
	void handleIllegalConstruction_returnsBadRequestAndMessage() {
		IllegalConstruction ex = new IllegalConstruction();
		ResponseEntity<Map<String, String>> resp = handler.handleIllegalConstruction(ex);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals("Illegal construction of expression", resp.getBody().get(ERROR_KEY));
	}

	@Test
	void handleUnsupportedMediaType_returns415AndMessage() {
		org.springframework.web.HttpMediaTypeNotSupportedException ex =
				new org.springframework.web.HttpMediaTypeNotSupportedException("text/plain");
		ResponseEntity<Map<String, String>> resp = handler.handleUnsupportedMediaType(ex);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals("Unsupported Media Type", resp.getBody().get(ERROR_KEY));
	}

	@Test
	void handleBadRequestMessage_returnsBadRequestAndMessage() {
		// Constructing HttpMessageNotReadableException varies between Spring versions; pass null instead
		ResponseEntity<Map<String, String>> resp = handler.handleBadRequestMessage(null);

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals("Malformed JSON request", resp.getBody().get(ERROR_KEY));
	}

	@Test
	void handleOtherExceptions_returns500AndMessage() {
		Exception ex = new RuntimeException("boom");
		ResponseEntity<Map<String, String>> resp = handler.handleOtherExceptions(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals("Internal server error", resp.getBody().get(ERROR_KEY));
	}

	@Test
	void handleNoResourceFound_returns404AndMessage() {
		ResponseEntity<Map<String, String>> resp = handler.handleNoResourceFound(null);

		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals("Resource not found", resp.getBody().get(ERROR_KEY));
	}
}
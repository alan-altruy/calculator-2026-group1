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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@org.springframework.context.annotation.Import(RestExceptionHandler.class)
public class RestExceptionHandlerTest {

    @Autowired
    private WebApplicationContext wac;

    private org.springframework.test.web.servlet.MockMvc mockMvc;

    private static final String API_URL = "/api/v1/evaluate";


    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void malformedJsonTriggersBadRequestHandler() throws Exception {
        String badJson = "{"; // malformed JSON
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(badJson))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        assertThat(res.getResponse().getContentAsString()).contains("Malformed JSON request");
    }

    @Test
    void handlerReturnsExpectedErrorBody() throws Exception {
        // kept for readability; replaced by parameterized test below
    }
    @ParameterizedTest
    @MethodSource("badRequestCases")
    void testExceptionHandlerResponses(String body, int expectedStatus, String expectedContentFragment) throws Exception {
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(API_URL)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(expectedStatus);
        assertThat(res.getResponse().getContentAsString()).contains(expectedContentFragment);
    }

    private static Stream<Arguments> badRequestCases() {
        return Stream.of(
            arguments("{\"ast\":{\"type\":\"operation\",\"op\":\"foo\",\"args\":[]}}", 400, "Illegal construction of expression"),
            arguments("{\"ast\":{\"type\":\"operation\",\"op\":\"/\",\"args\":[{\"type\":\"number\",\"value\":1},{\"type\":\"number\",\"value\":0}]}}", 500, "Internal server error"),
            arguments("{\"ast\":{\"type\":\"operation\",\"op\":\"+\",\"args\":{\"type\":\"number\",\"value\":1}}}", 500, "Internal server error")
        );
    }
}

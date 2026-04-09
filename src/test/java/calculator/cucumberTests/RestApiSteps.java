package calculator.cucumberTests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import calculator.CalculatorApplication;

public class RestApiSteps {

    private ConfigurableApplicationContext springContext;
    private MockMvc mockMvc;
    private String response;
    private int result;
    private int httpStatus;

    @Given("I start the REST API server")
    public void givenIStartTheRestApiServer() {
        // Démarre un contexte Spring Boot embarqué en mode servlet et expose MockMvc pour les pas suivants
        if (this.springContext == null || !this.springContext.isActive()) {
            SpringApplication app = new SpringApplication(CalculatorApplication.class);
            app.setWebApplicationType(WebApplicationType.SERVLET);
            // bind to a random port to avoid collisions with other processes/tests
            // pass as command-line arg to ensure higher precedence than application.properties
            this.springContext = app.run(new String[]{"--server.port=0"});
        }

        if (!(springContext instanceof WebApplicationContext)) {
            throw new IllegalStateException("Started application context is not a WebApplicationContext");
        }
        WebApplicationContext wac = (WebApplicationContext) springContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @When("I send a POST request to evaluate with the following JSON body {string}")
        public void whenISendAPostRequestToEvaluateWithTheFollowingJsonBody(String jsonBody) throws Exception {
        var mvcResult = this.mockMvc
            .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andReturn();
        response = mvcResult.getResponse().getContentAsString();
        httpStatus = mvcResult.getResponse().getStatus();
    }

    @Then("the response status code is {int}")
    public void thenTheResponseShouldBe(int expectedStatusCode) {
        assertNotNull(response);
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String resultStr = jsonResponse.optString("result", null);
            if (resultStr != null && !resultStr.isEmpty()) {
                result = Integer.parseInt(resultStr);
            }
        } catch (org.json.JSONException e) {
            throw new RuntimeException("Response is not a valid JSON", e);
        }

        assertEquals(expectedStatusCode, httpStatus);
    }

    @And("the response body is {string}")
    public void thenTheResponseBodyShouldBe(String expectedResponseBody) {
        assertEquals(expectedResponseBody, String.valueOf(result));
    }
}

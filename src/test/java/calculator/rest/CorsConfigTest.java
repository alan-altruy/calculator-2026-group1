package calculator.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@org.springframework.context.annotation.Import(CorsConfig.class)
class CorsConfigTest {

    @Autowired
    private WebApplicationContext wac;

    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void corsAllowsAnyOrigin() throws Exception {
        String body = "{\"ast\":{\"type\":\"number\",\"value\":1}}";
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/evaluate")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .header("Origin", "http://localhost:8080")
                .content(body))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        assertThat(res.getResponse().getHeader("Access-Control-Allow-Origin")).isEqualTo("http://localhost:8080");
    }
}

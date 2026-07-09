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
        var res = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options("/api/v1/evaluate")
                .header("Origin", "http://localhost:8080")
            .header("Access-Control-Request-Method", "POST"))
                .andReturn();
        int status = res.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        assertThat(res.getResponse().getHeader("Access-Control-Allow-Origin")).isEqualTo("http://localhost:8080");
    }
}

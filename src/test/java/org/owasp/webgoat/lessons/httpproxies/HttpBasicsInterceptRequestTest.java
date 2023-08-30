

package org.owasp.webgoat.lessons.httpproxies;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class HttpBasicsInterceptRequestTest extends AssignmentEndpointTest {

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    HttpBasicsInterceptRequest httpBasicsInterceptRequest = new HttpBasicsInterceptRequest();
    init(httpBasicsInterceptRequest);
    this.mockMvc = standaloneSetup(httpBasicsInterceptRequest).build();
  }

  @Test
  public void success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.success"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void failure() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void missingParam() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void missingHeader() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void whenPostAssignmentShouldNotPass() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}

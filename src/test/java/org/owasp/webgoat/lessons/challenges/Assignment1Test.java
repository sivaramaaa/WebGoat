

package org.owasp.webgoat.lessons.challenges;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.net.InetAddress;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.lessons.challenges.challenge1.Assignment1;
import org.owasp.webgoat.lessons.challenges.challenge1.ImageServlet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
class Assignment1Test extends AssignmentEndpointTest {

  private MockMvc mockMvc;
  private Flags flags;

  @BeforeEach
  void setup() {
    flags = new Flags();
    Assignment1 assignment1 = new Assignment1(flags);
    init(assignment1);
    this.mockMvc = standaloneSetup(assignment1).build();
  }

  @Test
  void success() throws Exception {
    InetAddress addr = InetAddress.getLocalHost();
    String host = addr.getHostAddress();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/challenge/1")
                .header("X-Forwarded-For", host)
                .param("username", "admin")
                .param(
                    "password",
                    SolutionConstants.PASSWORD.replace(
                        "1234", String.format("%04d", ImageServlet.PINCODE))))
        .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("flag: " + flags.getFlag(1))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void wrongPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/challenge/1")
                .param("username", "admin")
                .param("password", "wrong"))
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}

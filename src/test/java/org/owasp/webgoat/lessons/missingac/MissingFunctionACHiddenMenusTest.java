

package org.owasp.webgoat.lessons.missingac;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class MissingFunctionACHiddenMenusTest extends AssignmentEndpointTest {

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    MissingFunctionACHiddenMenus hiddenMenus = new MissingFunctionACHiddenMenus();
    init(hiddenMenus);
    this.mockMvc = standaloneSetup(hiddenMenus).build();
  }

  @Test
  public void HiddenMenusSuccess() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/hidden-menu")
                .param("hiddenMenu1", "Users")
                .param("hiddenMenu2", "Config"))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("access-control.hidden-menus.success"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void HiddenMenusClose() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/hidden-menu")
                .param("hiddenMenu1", "Config")
                .param("hiddenMenu2", "Users"))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("access-control.hidden-menus.close"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void HiddenMenusFailure() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/hidden-menu")
                .param("hiddenMenu1", "Foo")
                .param("hiddenMenu2", "Bar"))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("access-control.hidden-menus.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}



package org.owasp.webgoat.lessons.xss;

import static org.mockito.Mockito.lenient;
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
public class DOMCrossSiteScriptingTest extends AssignmentEndpointTest {
  private MockMvc mockMvc;
  private String randVal = "12034837";

  @BeforeEach
  public void setup() {
    DOMCrossSiteScripting domXss = new DOMCrossSiteScripting();
    init(domXss);
    this.mockMvc = standaloneSetup(domXss).build();
    CrossSiteScripting xss = new CrossSiteScripting();
    lenient().when(userSessionData.getValue("randValue")).thenReturn(randVal);
  }

  @Test
  public void success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/CrossSiteScripting/phone-home-xss")
                .header("webgoat-requested-by", "dom-xss-vuln")
                .param("param1", "42")
                .param("param2", "24"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.output", CoreMatchers.containsString("phoneHome Response is " + randVal)))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void failure() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/CrossSiteScripting/phone-home-xss")
                .header("webgoat-requested-by", "wrong-value")
                .param("param1", "22")
                .param("param2", "20"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}

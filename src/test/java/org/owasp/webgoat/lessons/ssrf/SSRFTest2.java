

package org.owasp.webgoat.lessons.ssrf;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
public class SSRFTest2 extends LessonTest {

  @BeforeEach
  public void setup() {
    when(webSession.getCurrentLesson()).thenReturn(new SSRF());
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void modifyUrlIfconfigPro() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/SSRF/task2").param("url", "http://ifconfig.pro"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void modifyUrlCat() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/SSRF/task2").param("url", "images/cat.jpg"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }
}

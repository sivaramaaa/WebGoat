

package org.owasp.webgoat.lessons.DB;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.owasp.webgoat.lessons.db.introduction.DB;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class SqlLessonTest extends LessonTest {

  @BeforeEach
  public void setup() {
    when(webSession.getCurrentLesson()).thenReturn(new DB());
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }
}

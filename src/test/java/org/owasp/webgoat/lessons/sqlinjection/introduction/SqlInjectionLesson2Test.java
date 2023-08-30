

package org.owasp.webgoat.lessons.db.introduction;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.db.SqlLessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class DBLesson2Test extends SqlLessonTest {

  @Test
  public void solution() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/attack2")
                .param("query", "SELECT department FROM employees WHERE userid=96134;"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }
}

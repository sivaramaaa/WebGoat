

package org.owasp.webgoat.lessons.db.introduction;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.db.SqlLessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Benedikt Stuhrmann
 * @since 11/07/18.
 */
public class DBLesson10Test extends SqlLessonTest {

  private String completedError = "JSON path \"lessonCompleted\"";

  @Test
  public void tableExistsIsFailure() throws Exception {
    try {
      mockMvc
          .perform(MockMvcRequestBuilders.post("/DB/attack10").param("action_string", ""))
          .andExpect(status().isOk())
          .andExpect(jsonPath("lessonCompleted", is(false)))
          .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.10.entries"))));
    } catch (AssertionError e) {
      if (!e.getMessage().contains(completedError)) throw e;

      mockMvc
          .perform(MockMvcRequestBuilders.post("/DB/attack10").param("action_string", ""))
          .andExpect(status().isOk())
          .andExpect(jsonPath("lessonCompleted", is(true)))
          .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.10.success"))));
    }
  }

  @Test
  public void tableMissingIsSuccess() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/attack10")
                .param("action_string", "%'; DROP TABLE access_log;--"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(true)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.10.success"))));
  }
}

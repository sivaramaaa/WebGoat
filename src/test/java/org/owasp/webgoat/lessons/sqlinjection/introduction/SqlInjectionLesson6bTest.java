

package org.owasp.webgoat.lessons.db.introduction;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.db.SqlLessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class DBLesson6bTest extends SqlLessonTest {

  @Test
  public void submitCorrectPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DBAdvanced/attack6b")
                .param("userid_6b", "passW0rD"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void submitWrongPassword() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DBAdvanced/attack6b")
                .param("userid_6b", "John"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }
}

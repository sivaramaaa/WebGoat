

package org.owasp.webgoat.lessons.db.introduction;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.db.SqlLessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class DBLesson5aTest extends SqlLessonTest {

  @Test
  public void knownAccountShouldDisplayData() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/assignment5a")
                .param("account", "Smith")
                .param("operator", "")
                .param("injection", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("assignment.not.solved"))))
        .andExpect(jsonPath("$.output", containsString("<p>USERID, FIRST_NAME")));
  }

  @Disabled
  @Test
  public void unknownAccount() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/assignment5a")
                .param("account", "Smith")
                .param("operator", "")
                .param("injection", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("NoResultsMatched"))))
        .andExpect(jsonPath("$.output").doesNotExist());
  }

  @Test
  public void DB() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/assignment5a")
                .param("account", "'")
                .param("operator", "OR")
                .param("injection", "'1' = '1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(true)))
        .andExpect(jsonPath("$.feedback", containsString("You have succeed")))
        .andExpect(jsonPath("$.output").exists());
  }

  @Test
  public void DBWrongShouldDisplayError() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/assignment5a")
                .param("account", "Smith'")
                .param("operator", "OR")
                .param("injection", "'1' = '1'"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(false)))
        .andExpect(
            jsonPath("$.feedback", containsString(messages.getMessage("assignment.not.solved"))))
        .andExpect(
            jsonPath(
                "$.output",
                is(
                    "malformed string: '1''<br> Your query was: SELECT * FROM user_data WHERE"
                        + " first_name = 'John' and last_name = 'Smith' OR '1' = '1''")));
  }
}

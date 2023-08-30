

package org.owasp.webgoat.lessons.db.introduction;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.lessons.db.SqlLessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class DBLesson5Test extends SqlLessonTest {

  @Autowired private LessonDataSource dataSource;

  @AfterEach
  public void removeGrant() throws SQLException {
    dataSource
        .getConnection()
        .prepareStatement("revoke select on grant_rights from unauthorized_user cascade")
        .execute();
  }

  @Test
  public void grantSolution() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/attack5")
                .param("query", "grant select on grant_rights to unauthorized_user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void differentTableShouldNotSolveIt() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/attack5")
                .param("query", "grant select on users to unauthorized_user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void noGrantShouldNotSolveIt() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/DB/attack5")
                .param("query", "select * from grant_rights"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}

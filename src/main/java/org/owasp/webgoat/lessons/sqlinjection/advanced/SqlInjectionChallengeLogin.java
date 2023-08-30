package org.owasp.webgoat.lessons.db.advanced;

import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBChallengeLogin extends AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public DBChallengeLogin(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/DBAdvanced/challenge_Login")
  @ResponseBody
  public AttackResult login(
      @RequestParam String username_login, @RequestParam String password_login) throws Exception {
    try (var connection = dataSource.getConnection()) {
      var statement =
          connection.prepareStatement(
              "select password from sql_challenge_users where userid = ? and password = ?");
      statement.setString(1, username_login);
      statement.setString(2, password_login);
      var resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return ("tom".equals(username_login))
            ? success(this).build()
            : failed(this).feedback("ResultsButNotTom").build();
      } else {
        return failed(this).feedback("NoResultsMatched").build();
      }
    }
  }
}

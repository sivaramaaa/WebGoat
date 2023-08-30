

package org.owasp.webgoat.lessons.db.mitigation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DBLesson13 extends AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public DBLesson13(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/DBMitigations/attack12a")
  @ResponseBody
  public AttackResult completed(@RequestParam String ip) {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement =
            connection.prepareStatement("select ip from servers where ip = ? and hostname = ?")) {
      preparedStatement.setString(1, ip);
      preparedStatement.setString(2, "webgoat-prd");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return success(this).build();
      }
      return failed(this).build();
    } catch (SQLException e) {
      log.error("Failed", e);
      return (failed(this).build());
    }
  }
}

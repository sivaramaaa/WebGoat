

package org.owasp.webgoat.lessons.db.introduction;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBLesson2 extends AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public DBLesson2(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/DB/attack2")
  @ResponseBody
  public AttackResult completed(@RequestParam String query) {
    return injectableQuery(query);
  }

  protected AttackResult injectableQuery(String query) {
    try (var connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
      ResultSet results = statement.executeQuery(query);
      StringBuilder output = new StringBuilder();

      results.first();

      if (results.getString("department").equals("Marketing")) {
        output.append("<span class='feedback-positive'>" + query + "</span>");
        output.append(DBLesson8.generateTable(results));
        return success(this).feedback("sql-injection.2.success").output(output.toString()).build();
      } else {
        return failed(this).feedback("sql-injection.2.failed").output(output.toString()).build();
      }
    } catch (SQLException sqle) {
      return failed(this).feedback("sql-injection.2.failed").output(sqle.getMessage()).build();
    }
  }
}

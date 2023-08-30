

package org.owasp.webgoat.lessons.db.introduction;

import static org.hsqldb.jdbc.JDBCResultSet.CONCUR_UPDATABLE;
import static org.hsqldb.jdbc.JDBCResultSet.TYPE_SCROLL_SENSITIVE;

import java.sql.Connection;
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
public class DBLesson9 extends AssignmentEndpoint {

  private final LessonDataSource dataSource;

  public DBLesson9(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/DB/attack9")
  @ResponseBody
  public AttackResult completed(@RequestParam String name, @RequestParam String auth_tan) {
    return injectableQueryIntegrity(name, auth_tan);
  }

  protected AttackResult injectableQueryIntegrity(String name, String auth_tan) {
    StringBuilder output = new StringBuilder();
    String query =
        "SELECT * FROM employees WHERE last_name = '"
            + name
            + "' AND auth_tan = '"
            + auth_tan
            + "'";
    try (Connection connection = dataSource.getConnection()) {
      try {
        Statement statement = connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);
        DBLesson8.log(connection, query);
        ResultSet results = statement.executeQuery(query);
        var test = results.getRow() != 0;
        if (results.getStatement() != null) {
          if (results.first()) {
            output.append(DBLesson8.generateTable(results));
          } else {
            // no results
            return failed(this).feedback("sql-injection.8.no.results").build();
          }
        }
      } catch (SQLException e) {
        System.err.println(e.getMessage());
        return failed(this)
            .output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>")
            .build();
      }

      return checkSalaryRanking(connection, output);

    } catch (Exception e) {
      System.err.println(e.getMessage());
      return failed(this)
          .output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>")
          .build();
    }
  }

  private AttackResult checkSalaryRanking(Connection connection, StringBuilder output) {
    try {
      String query = "SELECT * FROM employees ORDER BY salary DESC";
      try (Statement statement =
          connection.createStatement(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE); ) {
        ResultSet results = statement.executeQuery(query);

        results.first();
        // user completes lesson if John Smith is the first in the list
        if ((results.getString(2).equals("John")) && (results.getString(3).equals("Smith"))) {
          output.append(DBLesson8.generateTable(results));
          return success(this)
              .feedback("sql-injection.9.success")
              .output(output.toString())
              .build();
        } else {
          return failed(this).feedback("sql-injection.9.one").output(output.toString()).build();
        }
      }
    } catch (SQLException e) {
      return failed(this)
          .output("<br><span class='feedback-negative'>" + e.getMessage() + "</span>")
          .build();
    }
  }
}

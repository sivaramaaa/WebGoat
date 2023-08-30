package org.owasp.webgoat.lessons.db.advanced;

import java.sql.*;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.db.introduction.DBLesson5a;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBLesson6a extends AssignmentEndpoint {

  private final LessonDataSource dataSource;
  private static final String YOUR_QUERY_WAS = "<br> Your query was: ";

  public DBLesson6a(LessonDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostMapping("/DBAdvanced/attack6a")
  @ResponseBody
  public AttackResult completed(@RequestParam(value = "userid_6a") String userId) {
    return injectableQuery(userId);
    // The answer: Smith' union select userid,user_name, password,cookie,cookie, cookie,userid from
    // user_system_data --
  }

  public AttackResult injectableQuery(String accountName) {
    String query = "";
    try (Connection connection = dataSource.getConnection()) {
      boolean usedUnion = true;
      query = "SELECT * FROM user_data WHERE last_name = '" + accountName + "'";
      // Check if Union is used
      if (!accountName.matches("(?i)(^[^-/*;)]*)(\\s*)UNION(.*$)")) {
        usedUnion = false;
      }
      try (Statement statement =
          connection.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
        ResultSet results = statement.executeQuery(query);

        if ((results != null) && results.first()) {
          ResultSetMetaData resultsMetaData = results.getMetaData();
          StringBuilder output = new StringBuilder();

          output.append(DBLesson5a.writeTable(results, resultsMetaData));

          String appendingWhenSucceded;
          if (usedUnion)
            appendingWhenSucceded =
                "Well done! Can you also figure out a solution, by appending a new SQL Statement?";
          else
            appendingWhenSucceded =
                "Well done! Can you also figure out a solution, by using a UNION?";
          results.last();

          if (output.toString().contains("dave") && output.toString().contains("passW0rD")) {
            output.append(appendingWhenSucceded);
            return success(this)
                .feedback("sql-injection.advanced.6a.success")
                .feedbackArgs(output.toString())
                .output(" Your query was: " + query)
                .build();
          } else {
            return failed(this).output(output.toString() + YOUR_QUERY_WAS + query).build();
          }
        } else {
          return failed(this)
              .feedback("sql-injection.advanced.6a.no.results")
              .output(YOUR_QUERY_WAS + query)
              .build();
        }
      } catch (SQLException sqle) {
        return failed(this).output(sqle.getMessage() + YOUR_QUERY_WAS + query).build();
      }
    } catch (Exception e) {
      return failed(this)
          .output(this.getClass().getName() + " : " + e.getMessage() + YOUR_QUERY_WAS + query)
          .build();
    }
  }
}

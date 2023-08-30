

package org.owasp.webgoat.lessons.db.mitigation;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.db.advanced.DBLesson6a;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqlOnlyInputValidationOnKeywords extends AssignmentEndpoint {

  private final DBLesson6a lesson6a;

  public SqlOnlyInputValidationOnKeywords(DBLesson6a lesson6a) {
    this.lesson6a = lesson6a;
  }

  @PostMapping("/SqlOnlyInputValidationOnKeywords/attack")
  @ResponseBody
  public AttackResult attack(
      @RequestParam("userid_sql_only_input_validation_on_keywords") String userId) {
    userId = userId.toUpperCase().replace("FROM", "").replace("SELECT", "");
    if (userId.contains(" ")) {
      return failed(this).feedback("SqlOnlyInputValidationOnKeywords-failed").build();
    }
    AttackResult attackResult = lesson6a.injectableQuery(userId);
    return new AttackResult(
        attackResult.isLessonCompleted(),
        attackResult.getFeedback(),
        attackResult.getOutput(),
        getClass().getSimpleName(),
        true);
  }
}

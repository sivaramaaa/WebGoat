

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
public class SqlOnlyInputValidation extends AssignmentEndpoint {

  private final DBLesson6a lesson6a;

  public SqlOnlyInputValidation(DBLesson6a lesson6a) {
    this.lesson6a = lesson6a;
  }

  @PostMapping("/SqlOnlyInputValidation/attack")
  @ResponseBody
  public AttackResult attack(@RequestParam("userid_sql_only_input_validation") String userId) {
    if (userId.contains(" ")) {
      return failed(this).feedback("SqlOnlyInputValidation-failed").build();
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

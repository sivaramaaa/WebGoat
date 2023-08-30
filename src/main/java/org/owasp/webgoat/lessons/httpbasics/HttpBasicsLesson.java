

package org.owasp.webgoat.lessons.httpbasics;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpBasicsLesson extends AssignmentEndpoint {

  @PostMapping("/HttpBasics/attack1")
  @ResponseBody
  public AttackResult completed(@RequestParam String person) {
    if (!person.isBlank()) {
      return success(this)
          .feedback("http-basics.reversed")
          .feedbackArgs(new StringBuilder(person).reverse().toString())
          .build();
    } else {
      return failed(this).feedback("http-basics.empty").build();
    }
  }
}

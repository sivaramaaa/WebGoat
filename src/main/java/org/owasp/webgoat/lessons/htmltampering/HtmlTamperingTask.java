

package org.owasp.webgoat.lessons.htmltampering;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HtmlTamperingTask extends AssignmentEndpoint {

  @PostMapping("/HtmlTampering/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String QTY, @RequestParam String Total) {
    if (Float.parseFloat(QTY) * 2999.99 > Float.parseFloat(Total) + 1) {
      return success(this).feedback("html-tampering.tamper.success").build();
    }
    return failed(this).feedback("html-tampering.tamper.failure").build();
  }
}

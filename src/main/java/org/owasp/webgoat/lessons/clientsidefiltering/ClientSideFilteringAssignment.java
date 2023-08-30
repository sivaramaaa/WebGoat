

package org.owasp.webgoat.lessons.clientsidefiltering;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientSideFilteringAssignment extends AssignmentEndpoint {

  @PostMapping("/clientSideFiltering/attack1")
  @ResponseBody
  public AttackResult completed(@RequestParam String answer) {
    return "450000".equals(answer)
        ? success(this).feedback("assignment.solved").build()
        : failed(this).feedback("ClientSideFiltering.incorrect").build();
  }
}

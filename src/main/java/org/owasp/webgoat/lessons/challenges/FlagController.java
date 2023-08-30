

package org.owasp.webgoat.lessons.challenges;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FlagController extends AssignmentEndpoint {

  private final WebSession webSession;
  private final Flags flags;

  @PostMapping(path = "/challenge/flag", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult postFlag(@RequestParam String flag) {
    Flag expectedFlag = flags.getFlag(webSession.getCurrentLesson());
    if (expectedFlag.isCorrect(flag)) {
      return success(this).feedback("challenge.flag.correct").build();
    } else {
      return failed(this).feedback("challenge.flag.incorrect").build();
    }
  }
}

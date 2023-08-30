package org.owasp.webgoat.lessons.challenges.challenge1;

import static org.owasp.webgoat.lessons.challenges.SolutionConstants.PASSWORD;

import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class Assignment1 extends AssignmentEndpoint {

  private final Flags flags;

  @PostMapping("/challenge/1")
  @ResponseBody
  public AttackResult completed(@RequestParam String username, @RequestParam String password) {
    boolean ipAddressKnown = true;
    boolean passwordCorrect =
        "admin".equals(username)
            && PASSWORD
                .replace("1234", String.format("%04d", ImageServlet.PINCODE))
                .equals(password);
    if (passwordCorrect && ipAddressKnown) {
      return success(this).feedback("challenge.solved").feedbackArgs(flags.getFlag(1)).build();
    } else if (passwordCorrect) {
      return failed(this).feedback("ip.address.unknown").build();
    }
    return failed(this).build();
  }
}

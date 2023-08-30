

package org.owasp.webgoat.lessons.missingac;

import static org.owasp.webgoat.lessons.missingac.MissingFunctionAC.PASSWORD_SALT_SIMPLE;

import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MissingFunctionACYourHash extends AssignmentEndpoint {

  private final MissingAccessControlUserRepository userRepository;

  @PostMapping(
      path = "/access-control/user-hash",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult simple(String userHash) {
    User user = userRepository.findByUsername("Jerry");
    DisplayUser displayUser = new DisplayUser(user, PASSWORD_SALT_SIMPLE);
    if (userHash.equals(displayUser.getUserHash())) {
      return success(this).feedback("access-control.hash.success").build();
    } else {
      return failed(this).build();
    }
  }
}

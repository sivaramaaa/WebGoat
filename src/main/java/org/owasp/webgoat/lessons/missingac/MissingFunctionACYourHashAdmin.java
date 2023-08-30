

package org.owasp.webgoat.lessons.missingac;

import static org.owasp.webgoat.lessons.missingac.MissingFunctionAC.PASSWORD_SALT_ADMIN;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MissingFunctionACYourHashAdmin extends AssignmentEndpoint {

  private final MissingAccessControlUserRepository userRepository;

  public MissingFunctionACYourHashAdmin(MissingAccessControlUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping(
      path = "/access-control/user-hash-fix",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult admin(String userHash) {
    // current user should be in the DB
    // if not admin then return 403

    var user = userRepository.findByUsername("Jerry");
    var displayUser = new DisplayUser(user, PASSWORD_SALT_ADMIN);
    if (userHash.equals(displayUser.getUserHash())) {
      return success(this).feedback("access-control.hash.success").build();
    } else {
      return failed(this).feedback("access-control.hash.close").build();
    }
  }
}

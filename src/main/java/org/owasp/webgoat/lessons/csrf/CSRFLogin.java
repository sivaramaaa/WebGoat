

package org.owasp.webgoat.lessons.csrf;

import jakarta.servlet.http.HttpServletRequest;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.UserTracker;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nbaars
 * @since 11/17/17.
 */
@RestController
public class CSRFLogin extends AssignmentEndpoint {

  private final UserTrackerRepository userTrackerRepository;

  public CSRFLogin(UserTrackerRepository userTrackerRepository) {
    this.userTrackerRepository = userTrackerRepository;
  }

  @PostMapping(
      path = "/csrf/login",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult completed(HttpServletRequest request) {
    String userName = request.getUserPrincipal().getName();
    if (userName.startsWith("csrf")) {
      markAssignmentSolvedWithRealUser(userName.substring("csrf-".length()));
      return success(this).feedback("csrf-login-success").build();
    }
    return failed(this).feedback("csrf-login-failed").feedbackArgs(userName).build();
  }

  private void markAssignmentSolvedWithRealUser(String username) {
    UserTracker userTracker = userTrackerRepository.findByUser(username);
    userTracker.assignmentSolved(
        getWebSession().getCurrentLesson(), this.getClass().getSimpleName());
    userTrackerRepository.save(userTracker);
  }
}

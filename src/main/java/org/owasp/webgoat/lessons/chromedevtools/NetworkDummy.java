

package org.owasp.webgoat.lessons.chromedevtools;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is just a class used to make the the HTTP request.
 *
 * @author TMelzer
 * @since 30.11.18
 */
@RestController
public class NetworkDummy extends AssignmentEndpoint {

  @PostMapping("/ChromeDevTools/dummy")
  @ResponseBody
  public AttackResult completed(@RequestParam String successMessage) {
    UserSessionData userSessionData = getUserSessionData();
    String answer = (String) userSessionData.getValue("randValue");

    if (successMessage != null && successMessage.equals(answer)) {
      return success(this).feedback("xss-dom-message-success").build();
    } else {
      return failed(this).feedback("xss-dom-message-failure").build();
    }
  }
}

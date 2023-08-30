

package org.owasp.webgoat.lessons.xss.stored;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 11/23/16. */
@RestController
public class StoredCrossSiteScriptingVerifier extends AssignmentEndpoint {

  @PostMapping("/CrossSiteScriptingStored/stored-xss-follow-up")
  @ResponseBody
  public AttackResult completed(@RequestParam String successMessage) {
    UserSessionData userSessionData = getUserSessionData();

    if (successMessage.equals(userSessionData.getValue("randValue"))) {
      return success(this).feedback("xss-stored-callback-success").build();
    } else {
      return failed(this).feedback("xss-stored-callback-failure").build();
    }
  }
}

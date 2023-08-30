

package org.owasp.webgoat.lessons.xss;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrossSiteScriptingLesson6a extends AssignmentEndpoint {
  @Autowired UserSessionData userSessionData;

  @PostMapping("/CrossSiteScripting/attack6a")
  @ResponseBody
  public AttackResult completed(@RequestParam String DOMTestRoute) {

    if (DOMTestRoute.matches("start\\.mvc#test(\\/|)")) {
      // return )
      return success(this).feedback("xss-reflected-6a-success").build();
    } else {
      return failed(this).feedback("xss-reflected-6a-failure").build();
    }
  }
}



package org.owasp.webgoat.lessons.xss;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 11/23/16. */
@RestController
public class DOMCrossSiteScriptingVerifier extends AssignmentEndpoint {

  @PostMapping("/CrossSiteScripting/dom-follow-up")
  @ResponseBody
  public AttackResult completed(@RequestParam String successMessage) {
    UserSessionData userSessionData = getUserSessionData();
    String answer = (String) userSessionData.getValue("randValue");

    if (successMessage.equals(answer)) {
      return success(this).feedback("xss-dom-message-success").build();
    } else {
      return failed(this).feedback("xss-dom-message-failure").build();
    }
  }
}
// something like ...
// http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E
// or
// http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>

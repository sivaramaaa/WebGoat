

package org.owasp.webgoat.lessons.xss;

import jakarta.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DOMCrossSiteScripting extends AssignmentEndpoint {

  @PostMapping("/CrossSiteScripting/phone-home-xss")
  @ResponseBody
  public AttackResult completed(
      @RequestParam Integer param1, @RequestParam Integer param2, HttpServletRequest request) {
    UserSessionData userSessionData = getUserSessionData();
    SecureRandom number = new SecureRandom();
    userSessionData.setValue("randValue", String.valueOf(number.nextInt()));

    if (param1 == 42
        && param2 == 24
        && request.getHeader("webgoat-requested-by").equals("dom-xss-vuln")) {
      return success(this)
          .output("phoneHome Response is " + userSessionData.getValue("randValue").toString())
          .build();
    } else {
      return failed(this).build();
    }
  }
}
// something like ...
// http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E--andMoreGarbageHere
// or
// http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>

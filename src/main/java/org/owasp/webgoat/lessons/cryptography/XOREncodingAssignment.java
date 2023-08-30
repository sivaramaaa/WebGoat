

package org.owasp.webgoat.lessons.cryptography;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XOREncodingAssignment extends AssignmentEndpoint {

  @PostMapping("/crypto/encoding/xor")
  @ResponseBody
  public AttackResult completed(@RequestParam String answer_pwd1) {
    if (answer_pwd1 != null && answer_pwd1.equals("databasepassword")) {
      return success(this).feedback("crypto-encoding-xor.success").build();
    }
    return failed(this).feedback("crypto-encoding-xor.empty").build();
  }
}

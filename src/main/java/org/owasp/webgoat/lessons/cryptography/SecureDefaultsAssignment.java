

package org.owasp.webgoat.lessons.cryptography;

import java.security.NoSuchAlgorithmException;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureDefaultsAssignment extends AssignmentEndpoint {

  @PostMapping("/crypto/secure/defaults")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String secretFileName, @RequestParam String secretText)
      throws NoSuchAlgorithmException {
    if (secretFileName != null && secretFileName.equals("default_secret")) {
      if (secretText != null
          && HashingAssignment.getHash(secretText, "SHA-256")
              .equalsIgnoreCase(
                  "34de66e5caf2cb69ff2bebdc1f3091ecf6296852446c718e38ebfa60e4aa75d2")) {
        return success(this).feedback("crypto-secure-defaults.success").build();
      } else {
        return failed(this).feedback("crypto-secure-defaults.messagenotok").build();
      }
    }
    return failed(this).feedback("crypto-secure-defaults.notok").build();
  }
}

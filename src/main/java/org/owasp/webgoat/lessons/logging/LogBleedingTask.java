

package org.owasp.webgoat.lessons.logging;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogBleedingTask extends AssignmentEndpoint {

  Logger log = LoggerFactory.getLogger(this.getClass().getName());
  private String password;

  @PostConstruct
  public void generatePassword() {
    password = UUID.randomUUID().toString();
    log.info(
        "Password for admin: {}",
        Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
  }

  @PostMapping("/LogSpoofing/log-bleeding")
  @ResponseBody
  public AttackResult completed(@RequestParam String username, @RequestParam String password) {
    if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
      return failed(this).output("Please provide username (Admin) and password").build();
    }

    if (username.equals("Admin") && password.equals(this.password)) {
      return success(this).build();
    }

    return failed(this).build();
  }
}

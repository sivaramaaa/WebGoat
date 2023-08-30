

package org.owasp.webgoat.lessons.authbypass;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 1/5/17. */
@RestController
public class VerifyAccount extends AssignmentEndpoint {

  @Autowired private WebSession webSession;

  @Autowired UserSessionData userSessionData;

  @PostMapping(
      path = "/apass/verify-account",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult completed(
      @RequestParam String userId, @RequestParam String verifyMethod, HttpServletRequest req)
      throws ServletException, IOException {
    AccountVerificationHelper verificationHelper = new AccountVerificationHelper();
    Map<String, String> submittedAnswers = parseSecQuestions(req);
    if (verificationHelper.didUserLikelylCheat((HashMap) submittedAnswers)) {
      return failed(this)
          .feedback("verify-account.cheated")
          .output("Yes, you guessed correctly, but see the feedback message")
          .build();
    }

    // else
    if (verificationHelper.verifyAccount(Integer.valueOf(userId), (HashMap) submittedAnswers)) {
      userSessionData.setValue("account-verified-id", userId);
      return success(this).feedback("verify-account.success").build();
    } else {
      return failed(this).feedback("verify-account.failed").build();
    }
  }

  private HashMap<String, String> parseSecQuestions(HttpServletRequest req) {
    Map<String, String> userAnswers = new HashMap<>();
    List<String> paramNames = Collections.list(req.getParameterNames());
    for (String paramName : paramNames) {
      // String paramName = req.getParameterNames().nextElement();
      if (paramName.contains("secQuestion")) {
        userAnswers.put(paramName, req.getParameter(paramName));
      }
    }
    return (HashMap) userAnswers;
  }
}

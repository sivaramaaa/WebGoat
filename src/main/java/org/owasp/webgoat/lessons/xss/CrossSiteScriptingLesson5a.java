

package org.owasp.webgoat.lessons.xss;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrossSiteScriptingLesson5a extends AssignmentEndpoint {

  public static final Predicate<String> XSS_PATTERN =
      Pattern.compile(
              ".*<script>(console\\.log|alert)\\(.*\\);?</script>.*", Pattern.CASE_INSENSITIVE)
          .asMatchPredicate();
  @Autowired UserSessionData userSessionData;

  @GetMapping("/CrossSiteScripting/attack5a")
  @ResponseBody
  public AttackResult completed(
      @RequestParam Integer QTY1,
      @RequestParam Integer QTY2,
      @RequestParam Integer QTY3,
      @RequestParam Integer QTY4,
      @RequestParam String field1,
      @RequestParam String field2) {

    if (XSS_PATTERN.test(field2)) {
      return failed(this).feedback("xss-reflected-5a-failed-wrong-field").build();
    }

    double totalSale =
        QTY1.intValue() * 69.99
            + QTY2.intValue() * 27.99
            + QTY3.intValue() * 1599.99
            + QTY4.intValue() * 299.99;

    userSessionData.setValue("xss-reflected1-complete", "false");
    StringBuilder cart = new StringBuilder();
    cart.append("Thank you for shopping at WebGoat. <br />Your support is appreciated<hr />");
    cart.append("<p>We have charged credit card:" + field1 + "<br />");
    cart.append("                             ------------------- <br />");
    cart.append("                               $" + totalSale);

    // init state
    if (userSessionData.getValue("xss-reflected1-complete") == null) {
      userSessionData.setValue("xss-reflected1-complete", "false");
    }

    if (XSS_PATTERN.test(field1)) {
      userSessionData.setValue("xss-reflected-5a-complete", "true");
      if (field1.toLowerCase().contains("console.log")) {
        return success(this)
            .feedback("xss-reflected-5a-success-console")
            .output(cart.toString())
            .build();
      } else {
        return success(this)
            .feedback("xss-reflected-5a-success-alert")
            .output(cart.toString())
            .build();
      }
    } else {
      userSessionData.setValue("xss-reflected1-complete", "false");
      return failed(this).feedback("xss-reflected-5a-failure").output(cart.toString()).build();
    }
  }
}

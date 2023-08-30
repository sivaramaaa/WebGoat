

package org.owasp.webgoat.lessons.missingac;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Created by jason on 1/5/17. */
@RestController
public class MissingFunctionACHiddenMenus extends AssignmentEndpoint {

  @PostMapping(
      path = "/access-control/hidden-menu",
      produces = {"application/json"})
  @ResponseBody
  public AttackResult completed(String hiddenMenu1, String hiddenMenu2) {
    if (hiddenMenu1.equals("Users") && hiddenMenu2.equals("Config")) {
      return success(this).output("").feedback("access-control.hidden-menus.success").build();
    }

    if (hiddenMenu1.equals("Config") && hiddenMenu2.equals("Users")) {
      return failed(this).output("").feedback("access-control.hidden-menus.close").build();
    }

    return failed(this).feedback("access-control.hidden-menus.failure").output("").build();
  }
}

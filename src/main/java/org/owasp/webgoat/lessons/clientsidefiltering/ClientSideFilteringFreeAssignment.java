

package org.owasp.webgoat.lessons.clientsidefiltering;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nbaars
 * @since 4/6/17.
 */
@RestController
public class ClientSideFilteringFreeAssignment extends AssignmentEndpoint {

  public static final String SUPER_COUPON_CODE = "get_it_for_free";

  @PostMapping("/clientSideFiltering/getItForFree")
  @ResponseBody
  public AttackResult completed(@RequestParam String checkoutCode) {
    if (SUPER_COUPON_CODE.equals(checkoutCode)) {
      return success(this).build();
    }
    return failed(this).build();
  }
}

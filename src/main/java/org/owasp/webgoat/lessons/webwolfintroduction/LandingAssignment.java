

package org.owasp.webgoat.lessons.webwolfintroduction;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class LandingAssignment extends AssignmentEndpoint {

  @Value("${webwolf.landingpage.url}")
  private String landingPageUrl;

  @PostMapping("/WebWolf/landing")
  @ResponseBody
  public AttackResult click(String uniqueCode) {
    if (StringUtils.reverse(getWebSession().getUserName()).equals(uniqueCode)) {
      return success(this).build();
    }
    return failed(this).feedback("webwolf.landing_wrong").build();
  }

  @GetMapping("/WebWolf/landing/password-reset")
  public ModelAndView openPasswordReset(HttpServletRequest request) throws URISyntaxException {
    URI uri = new URI(request.getRequestURL().toString());
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("webwolfUrl", landingPageUrl);
    modelAndView.addObject("uniqueCode", StringUtils.reverse(getWebSession().getUserName()));

    modelAndView.setViewName("lessons/webwolfintroduction/templates/webwolfPasswordReset.html");
    return modelAndView;
  }
}



package org.owasp.webgoat.lessons.passwordreset;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.passwordreset.resetlink.PasswordChangeForm;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class ResetLinkAssignment extends AssignmentEndpoint {

  private static final String VIEW_FORMATTER = "lessons/passwordreset/templates/%s.html";
  static final String PASSWORD_TOM_9 =
      "somethingVeryRandomWhichNoOneWillEverTypeInAsPasswordForTom";
  static final String TOM_EMAIL = "tom@webgoat-cloud.org";
  static Map<String, String> userToTomResetLink = new HashMap<>();
  static Map<String, String> usersToTomPassword = Maps.newHashMap();
  static List<String> resetLinks = new ArrayList<>();

  static final String TEMPLATE =
      """
          Hi, you requested a password reset link, please use this <a target='_blank'
           href='http://%s/WebGoat/PasswordReset/reset/reset-password/%s'>link</a> to reset your
           password.

          If you did not request this password change you can ignore this message.
          If you have any comments or questions, please do not hesitate to reach us at
           support@webgoat-cloud.org

          Kind regards,
          Team WebGoat
          """;

  @PostMapping("/PasswordReset/reset/login")
  @ResponseBody
  public AttackResult login(@RequestParam String password, @RequestParam String email) {
    if (TOM_EMAIL.equals(email)) {
      String passwordTom =
          usersToTomPassword.getOrDefault(getWebSession().getUserName(), PASSWORD_TOM_9);
      if (passwordTom.equals(PASSWORD_TOM_9)) {
        return failed(this).feedback("login_failed").build();
      } else if (passwordTom.equals(password)) {
        return success(this).build();
      }
    }
    return failed(this).feedback("login_failed.tom").build();
  }

  @GetMapping("/PasswordReset/reset/reset-password/{link}")
  public ModelAndView resetPassword(@PathVariable(value = "link") String link, Model model) {
    ModelAndView modelAndView = new ModelAndView();
    if (ResetLinkAssignment.resetLinks.contains(link)) {
      PasswordChangeForm form = new PasswordChangeForm();
      form.setResetLink(link);
      model.addAttribute("form", form);
      modelAndView.addObject("form", form);
      modelAndView.setViewName(
          VIEW_FORMATTER.formatted("password_reset")); // Display html page for changing password
    } else {
      modelAndView.setViewName(VIEW_FORMATTER.formatted("password_link_not_found"));
    }
    return modelAndView;
  }

  @PostMapping("/PasswordReset/reset/change-password")
  public ModelAndView changePassword(
      @ModelAttribute("form") PasswordChangeForm form, BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();
    if (!org.springframework.util.StringUtils.hasText(form.getPassword())) {
      bindingResult.rejectValue("password", "not.empty");
    }
    if (bindingResult.hasErrors()) {
      modelAndView.setViewName(VIEW_FORMATTER.formatted("password_reset"));
      return modelAndView;
    }
    if (!resetLinks.contains(form.getResetLink())) {
      modelAndView.setViewName(VIEW_FORMATTER.formatted("password_link_not_found"));
      return modelAndView;
    }
    if (checkIfLinkIsFromTom(form.getResetLink())) {
      usersToTomPassword.put(getWebSession().getUserName(), form.getPassword());
    }
    modelAndView.setViewName(VIEW_FORMATTER.formatted("success"));
    return modelAndView;
  }

  private boolean checkIfLinkIsFromTom(String resetLinkFromForm) {
    String resetLink = userToTomResetLink.getOrDefault(getWebSession().getUserName(), "unknown");
    return resetLink.equals(resetLinkFromForm);
  }
}

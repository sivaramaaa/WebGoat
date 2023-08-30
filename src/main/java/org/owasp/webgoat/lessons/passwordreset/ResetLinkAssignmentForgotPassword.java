

package org.owasp.webgoat.lessons.passwordreset;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Part of the password reset assignment. Used to send the e-mail.
 *
 * @author nbaars
 * @since 8/20/17.
 */
@RestController
public class ResetLinkAssignmentForgotPassword extends AssignmentEndpoint {

  private final RestTemplate restTemplate;
  private String webWolfHost;
  private String webWolfPort;
  private final String webWolfMailURL;

  public ResetLinkAssignmentForgotPassword(
      RestTemplate restTemplate,
      @Value("${webwolf.host}") String webWolfHost,
      @Value("${webwolf.port}") String webWolfPort,
      @Value("${webwolf.mail.url}") String webWolfMailURL) {
    this.restTemplate = restTemplate;
    this.webWolfHost = webWolfHost;
    this.webWolfPort = webWolfPort;
    this.webWolfMailURL = webWolfMailURL;
  }

  @PostMapping("/PasswordReset/ForgotPassword/create-password-reset-link")
  @ResponseBody
  public AttackResult sendPasswordResetLink(
      @RequestParam String email, HttpServletRequest request) {
    String resetLink = UUID.randomUUID().toString();
    ResetLinkAssignment.resetLinks.add(resetLink);
    String host = request.getHeader("host");
    if (ResetLinkAssignment.TOM_EMAIL.equals(email)
        && (host.contains(webWolfPort)
            || host.contains(webWolfHost))) { // User indeed changed the host header.
      ResetLinkAssignment.userToTomResetLink.put(getWebSession().getUserName(), resetLink);
      fakeClickingLinkEmail(host, resetLink);
    } else {
      try {
        sendMailToUser(email, host, resetLink);
      } catch (Exception e) {
        return failed(this).output("E-mail can't be send. please try again.").build();
      }
    }

    return success(this).feedback("email.send").feedbackArgs(email).build();
  }

  private void sendMailToUser(String email, String host, String resetLink) {
    int index = email.indexOf("@");
    String username = email.substring(0, index == -1 ? email.length() : index);
    PasswordResetEmail mail =
        PasswordResetEmail.builder()
            .title("Your password reset link")
            .contents(String.format(ResetLinkAssignment.TEMPLATE, host, resetLink))
            .sender("password-reset@webgoat-cloud.net")
            .recipient(username)
            .build();
    this.restTemplate.postForEntity(webWolfMailURL, mail, Object.class);
  }

  private void fakeClickingLinkEmail(String host, String resetLink) {
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      HttpEntity httpEntity = new HttpEntity(httpHeaders);
      new RestTemplate()
          .exchange(
              String.format("http://%s/PasswordReset/reset/reset-password/%s", host, resetLink),
              HttpMethod.GET,
              httpEntity,
              Void.class);
    } catch (Exception e) {
      // don't care
    }
  }
}

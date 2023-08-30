

package org.owasp.webgoat.webwolf.mailbox;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class MailboxController {

  private final MailboxRepository mailboxRepository;

  @GetMapping("/mail")
  public ModelAndView mail() {
    UserDetails user =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ModelAndView modelAndView = new ModelAndView();
    List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc(user.getUsername());
    if (emails != null && !emails.isEmpty()) {
      modelAndView.addObject("total", emails.size());
      modelAndView.addObject("emails", emails);
    }
    modelAndView.setViewName("mailbox");
    return modelAndView;
  }

  @PostMapping("/mail")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmail(@RequestBody Email email) {
    mailboxRepository.save(email);
  }

  @DeleteMapping("/mail")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteAllMail() {
    mailboxRepository.deleteAll();
  }
}

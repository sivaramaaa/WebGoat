

package org.owasp.webgoat.webwolf.mailbox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MailboxRepositoryTest {

  @Autowired private MailboxRepository mailboxRepository;

  @Test
  void emailShouldBeSaved() {
    Email email = new Email();
    email.setTime(LocalDateTime.now());
    email.setTitle("test");
    email.setSender("test@test.com");
    email.setContents("test");
    email.setRecipient("someone@webwolf.org");
    mailboxRepository.save(email);
  }

  @Test
  void savedEmailShouldBeFoundByReceipient() {
    Email email = new Email();
    email.setTime(LocalDateTime.now());
    email.setTitle("test");
    email.setSender("test@test.com");
    email.setContents("test");
    email.setRecipient("someone@webwolf.org");
    mailboxRepository.saveAndFlush(email);

    List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc("someone@webwolf.org");

    assertEquals(emails.size(), 1);
  }
}

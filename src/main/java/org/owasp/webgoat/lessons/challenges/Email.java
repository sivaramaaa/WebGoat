

package org.owasp.webgoat.lessons.challenges;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Email implements Serializable {

  private LocalDateTime time;
  private String contents;
  private String sender;
  private String title;
  private String recipient;
}

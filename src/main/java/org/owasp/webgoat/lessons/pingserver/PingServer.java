package org.owasp.webgoat.lessons.pingserver;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class PingServer extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A10;
  }

  @Override
  public String getTitle() {
    return "ping.title";
  }
}

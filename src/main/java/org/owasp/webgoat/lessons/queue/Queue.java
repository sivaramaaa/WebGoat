package org.owasp.webgoat.lessons.queue;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class Queue extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A8;
  }

  @Override
  public String getTitle() {
    return "Queue.title";
  }
}

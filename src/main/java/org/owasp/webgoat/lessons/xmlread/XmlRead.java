package org.owasp.webgoat.lessons.xmlread;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class XmlRead extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.A5;
  }

  @Override
  public String getTitle() {
    return "xxe.title";
  }
}

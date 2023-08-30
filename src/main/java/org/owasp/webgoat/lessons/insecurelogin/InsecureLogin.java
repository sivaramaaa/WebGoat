package org.owasp.webgoat.lessons.insecurelogin;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class InsecureLogin extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A7;
  }

  @Override
  public String getTitle() {
    return "insecure-login.title";
  }
}

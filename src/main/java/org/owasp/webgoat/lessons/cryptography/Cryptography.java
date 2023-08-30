

package org.owasp.webgoat.lessons.cryptography;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class Cryptography extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A2;
  }

  @Override
  public String getTitle() {
    return "6.crypto.title"; // first lesson in general
  }
}

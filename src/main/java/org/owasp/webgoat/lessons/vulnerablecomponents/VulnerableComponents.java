

package org.owasp.webgoat.lessons.vulnerablecomponents;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class VulnerableComponents extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A6;
  }

  @Override
  public String getTitle() {
    return "vulnerable-components.title";
  }
}



package org.owasp.webgoat.lessons.csrf;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

/** Created by jason on 9/29/17. */
@Component
public class CSRF extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.A10;
  }

  @Override
  public String getTitle() {
    return "csrf.title";
  }
}

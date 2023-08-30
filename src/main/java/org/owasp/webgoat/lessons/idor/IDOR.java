package org.owasp.webgoat.lessons.idor;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

 misfir3
 * @version $Id: $Id
 * @since January 3, 2017
 */
@Component
public class IDOR extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.A1;
  }

  @Override
  public String getTitle() {
    return "idor.title";
  }
}

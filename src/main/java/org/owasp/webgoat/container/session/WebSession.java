package org.owasp.webgoat.container.session;

import java.io.Serializable;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;


public class WebSession implements Serializable {

  private static final long serialVersionUID = -4270066103101711560L;
  private final WebGoatUser currentUser;
  private transient Lesson currentLesson;
  private boolean securityEnabled;

  public WebSession() {
    this.currentUser =
        (WebGoatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  /**
   * Setter for the field <code>currentScreen</code>.
   *
   * @param lesson current lesson
   */
  public void setCurrentLesson(Lesson lesson) {
    this.currentLesson = lesson;
  }

  /**
   * getCurrentLesson.
   *
   * @return a {@link Lesson} object.
   */
  public Lesson getCurrentLesson() {
    return this.currentLesson;
  }

  /**
   * Gets the userName attribute of the WebSession object
   *
   * @return The userName value
   */
  public String getUserName() {
    return currentUser.getUsername();
  }

  public WebGoatUser getUser() {
    return currentUser;
  }

  public void toggleSecurity() {
    this.securityEnabled = !this.securityEnabled;
  }

  public boolean isSecurityEnabled() {
    return securityEnabled;
  }
}

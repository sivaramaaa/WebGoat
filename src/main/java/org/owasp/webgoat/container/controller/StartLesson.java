
package org.owasp.webgoat.container.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StartLesson {

  private final WebSession ws;
  private final Course course;

  public StartLesson(WebSession ws, Course course) {
    this.ws = ws;
    this.course = course;
  }

  /**
   * start.
   *
   * @return a {@link ModelAndView} object.
   */
  @RequestMapping(
      path = "startlesson.mvc",
      method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView start() {
    var model = new ModelAndView();

    model.addObject("course", course);
    model.addObject("lesson", ws.getCurrentLesson());
    model.setViewName("lesson_content");

    return model;
  }

  @RequestMapping(
      value = {"*.lesson"},
      produces = "text/html")
  public ModelAndView lessonPage(HttpServletRequest request) {
    var model = new ModelAndView("lesson_content");
    var path = request.getRequestURL().toString(); // we now got /a/b/c/AccessControlMatrix.lesson
    var lessonName = path.substring(path.lastIndexOf('/') + 1, path.indexOf(".lesson"));

    course.getLessons().stream()
        .filter(l -> l.getId().equals(lessonName))
        .findFirst()
        .ifPresent(
            lesson -> {
              ws.setCurrentLesson(lesson);
              model.addObject("lesson", lesson);
            });

    return model;
  }
}

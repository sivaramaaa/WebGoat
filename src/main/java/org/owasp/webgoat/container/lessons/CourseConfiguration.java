

package org.owasp.webgoat.container.lessons;

import static java.util.stream.Collectors.groupingBy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.Course;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Configuration
public class CourseConfiguration {

  private final List<Lesson> lessons;
  private final List<AssignmentEndpoint> assignments;
  private final Map<String, List<AssignmentEndpoint>> assignmentsByPackage;

  public CourseConfiguration(List<Lesson> lessons, List<AssignmentEndpoint> assignments) {
    this.lessons = lessons;
    this.assignments = assignments;
    assignmentsByPackage =
        this.assignments.stream().collect(groupingBy(a -> a.getClass().getPackageName()));
  }

  @Bean
  public Course course() {
    lessons.stream().forEach(l -> l.setAssignments(createAssignment(l)));
    return new Course(lessons);
  }

  private List<Assignment> createAssignment(Lesson lesson) {
    var endpoints = assignmentsByPackage.get(lesson.getClass().getPackageName());
    if (CollectionUtils.isEmpty(endpoints)) {
      log.warn("Lesson: {} has no endpoints, is this intentionally?", lesson.getTitle());
      return new ArrayList<>();
    }
    return endpoints.stream()
        .map(
            e ->
                new Assignment(
                    e.getClass().getSimpleName(), getPath(e.getClass()), getHints(e.getClass())))
        .toList();
  }

  private String getPath(Class<? extends AssignmentEndpoint> e) {
    for (Method m : e.getMethods()) {
      if (methodReturnTypeIsOfTypeAttackResult(m)) {
        var mapping = getMapping(m);
        if (mapping != null) {
          return mapping;
        }
      }
    }
    throw new IllegalStateException(
        "Assignment endpoint: "
            + e
            + " has no mapping like @GetMapping/@PostMapping etc,with return type 'AttackResult' or"
            + " 'ResponseEntity<AttackResult>' please consider adding one");
  }

  private boolean methodReturnTypeIsOfTypeAttackResult(Method m) {
    if (m.getReturnType() == AttackResult.class) {
      return true;
    }
    var genericType = m.getGenericReturnType();
    if (genericType instanceof ParameterizedType) {
      return ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0]
          == AttackResult.class;
    }
    return false;
  }

  private String getMapping(Method m) {
    String[] paths = null;
    // Find the path, either it is @GetMapping("/attack") of GetMapping(path = "/attack") both are
    // valid, we need to consider both
    if (m.getAnnotation(RequestMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(RequestMapping.class).value(),
              m.getAnnotation(RequestMapping.class).path());
    } else if (m.getAnnotation(PostMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(PostMapping.class).value(),
              m.getAnnotation(PostMapping.class).path());
    } else if (m.getAnnotation(GetMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(GetMapping.class).value(), m.getAnnotation(GetMapping.class).path());
    } else if (m.getAnnotation(PutMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(PutMapping.class).value(), m.getAnnotation(PutMapping.class).path());
    }
    if (paths == null) {
      return null;
    } else {
      return Arrays.stream(paths).filter(path -> !"".equals(path)).findFirst().orElse("");
    }
  }

  private List<String> getHints(Class<? extends AssignmentEndpoint> e) {
    if (e.isAnnotationPresent(AssignmentHints.class)) {
      return List.of(e.getAnnotationsByType(AssignmentHints.class)[0].value());
    }
    return Collections.emptyList();
  }
}

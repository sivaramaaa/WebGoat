
package org.owasp.webgoat.container.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.container.service.LessonMenuService.URL_LESSONMENU_MVC;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.beust.jcommander.internal.Lists;
import java.util.Arrays;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.LessonTracker;
import org.owasp.webgoat.container.users.UserTracker;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class LessonMenuServiceTest {

  @Mock(lenient = true)
  private LessonTracker lessonTracker;

  @Mock private Course course;
  @Mock private UserTracker userTracker;
  @Mock private UserTrackerRepository userTrackerRepository;
  @Mock private WebSession webSession;
  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    this.mockMvc =
        standaloneSetup(
                new LessonMenuService(
                    course,
                    webSession,
                    userTrackerRepository,
                    Arrays.asList("none"),
                    Arrays.asList("none")))
            .build();
  }

  @Test
  void lessonsShouldBeOrdered() throws Exception {
    Lesson l1 = Mockito.mock(Lesson.class);
    Lesson l2 = Mockito.mock(Lesson.class);
    when(l1.getTitle()).thenReturn("ZA");
    when(l2.getTitle()).thenReturn("AA");
    when(lessonTracker.isLessonSolved()).thenReturn(false);
    when(course.getLessons(any())).thenReturn(Lists.newArrayList(l1, l2));
    when(course.getCategories()).thenReturn(Lists.newArrayList(Category.A1));
    when(userTracker.getLessonTracker(any(Lesson.class))).thenReturn(lessonTracker);
    when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);

    mockMvc
        .perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].children[0].name", CoreMatchers.is("AA")))
        .andExpect(jsonPath("$[0].children[1].name", CoreMatchers.is("ZA")));
  }

  @Test
  void lessonCompleted() throws Exception {
    Lesson l1 = Mockito.mock(Lesson.class);
    when(l1.getTitle()).thenReturn("ZA");
    when(lessonTracker.isLessonSolved()).thenReturn(true);
    when(course.getLessons(any())).thenReturn(Lists.newArrayList(l1));
    when(course.getCategories()).thenReturn(Lists.newArrayList(Category.A1));
    when(userTracker.getLessonTracker(any(Lesson.class))).thenReturn(lessonTracker);
    when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);

    mockMvc
        .perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
        .andExpect(status().isOk()) // .andDo(print())
        .andExpect(jsonPath("$[0].children[0].complete", CoreMatchers.is(true)));
  }
}

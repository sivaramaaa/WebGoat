package org.owasp.webgoat.container.service;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.LessonTracker;
import org.owasp.webgoat.container.users.UserTracker;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

 nbaars
 * @version $Id: $Id
 * @since November 25, 2016
 */
@ExtendWith(MockitoExtension.class)
class LessonProgressServiceTest {

  private MockMvc mockMvc;

  @Mock private Lesson lesson;
  @Mock private UserTracker userTracker;
  @Mock private LessonTracker lessonTracker;
  @Mock private UserTrackerRepository userTrackerRepository;
  @Mock private WebSession websession;

  @BeforeEach
  void setup() {
    Assignment assignment = new Assignment("test", "test", List.of());
    when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);
    when(userTracker.getLessonTracker(any(Lesson.class))).thenReturn(lessonTracker);
    when(websession.getCurrentLesson()).thenReturn(lesson);
    when(lessonTracker.getLessonOverview()).thenReturn(Maps.newHashMap(assignment, true));
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(
                new LessonProgressService(userTrackerRepository, websession))
            .build();
  }

  @Test
  void jsonLessonOverview() throws Exception {
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get("/service/lessonoverview.mvc")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].assignment.name", is("test")))
        .andExpect(jsonPath("$[0].solved", is(true)));
  }
}

package org.owasp.webgoat;

import java.util.Map;
import org.junit.jupiter.api.Test;


class SessionManagementIT extends IntegrationTest {

  private static final String HIJACK_LOGIN_CONTEXT_PATH = "/WebGoat/HijackSession/login";

  @Test
  void hijackSessionTest() {
    startLesson("HijackSession");

    checkAssignment(
        url(HIJACK_LOGIN_CONTEXT_PATH),
        Map.of("username", "webgoat", "password", "webgoat"),
        false);
  }
}



package org.owasp.webgoat.lessons.xss;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrossSiteScriptingLesson4 extends AssignmentEndpoint {

  @PostMapping("/CrossSiteScripting/attack4")
  @ResponseBody
  public AttackResult completed(@RequestParam String editor2) {

    String editor = editor2.replaceAll("\\<.*?>", "");

    if ((editor.contains("Policy.getInstance(\"antisamy-slashdot.xml\"")
            || editor.contains(".scan(newComment, \"antisamy-slashdot.xml\"")
            || editor.contains(".scan(newComment, new File(\"antisamy-slashdot.xml\")"))
        && editor.contains("new AntiSamy();")
        && editor.contains(".scan(newComment,")
        && editor.contains("CleanResults")
        && editor.contains("MyCommentDAO.addComment(threadID, userID")
        && editor.contains(".getCleanHTML());")) {
      return success(this).feedback("xss-mitigation-4-success").build();
    } else {
      return failed(this).feedback("xss-mitigation-4-failed").build();
    }
  }
}

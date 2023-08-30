package org.owasp.webgoat.lessons.xmlread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public class Ping {

  @Value("${webgoat.user.directory}")
  private String webGoatHomeDirectory;

  @Autowired private WebSession webSession;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public String logRequest(
      @RequestHeader("User-Agent") String userAgent, @RequestParam(required = false) String text) {
    String logLine = String.format("%s %s %s", "GET", userAgent, text);
    log.debug(logLine);
    File logFile = new File(webGoatHomeDirectory, "/xmlread/log" + webSession.getUserName() + ".txt");
    try {
      try (PrintWriter pw = new PrintWriter(logFile)) {
        pw.println(logLine);
      }
    } catch (FileNotFoundException e) {
      log.error("Error occurred while writing the logfile", e);
    }
    return "";
  }
}

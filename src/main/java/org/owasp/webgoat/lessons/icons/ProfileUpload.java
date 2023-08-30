package org.owasp.webgoat.lessons.icons;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProfileUpload extends ProfileUploadBase {

  public ProfileUpload(
      @Value("${webgoat.server.directory}") String webGoatHomeDirectory, WebSession webSession) {
    super(webGoatHomeDirectory, webSession);
  }

  @PostMapping(
      value = "/icons/profile-upload",
      consumes = ALL_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult uploadFileHandler(
      @RequestParam("uploadedFile") MultipartFile file,
      @RequestParam(value = "fullName", required = false) String fullName) {
    return super.execute(file, fullName);
  }

  @GetMapping("/icons/profile-picture")
  @ResponseBody
  public ResponseEntity<?> getProfilePicture() {
    return super.getProfilePicture();
  }
}

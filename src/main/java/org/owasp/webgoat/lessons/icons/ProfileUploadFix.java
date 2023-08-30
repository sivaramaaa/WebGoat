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
public class ProfileUploadFix extends ProfileUploadBase {

  public ProfileUploadFix(
      @Value("${webgoat.server.directory}") String webGoatHomeDirectory, WebSession webSession) {
    super(webGoatHomeDirectory, webSession);
  }

  @PostMapping(
      value = "/Icons/profile-upload-fix",
      consumes = ALL_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult uploadFileHandler(
      @RequestParam("uploadedFileFix") MultipartFile file,
      @RequestParam(value = "fullNameFix", required = false) String fullName) {
    return super.execute(file, fullName != null ? fullName.replace("../", "") : "");
  }

  @GetMapping("/Icons/profile-picture-fix")
  @ResponseBody
  public ResponseEntity<?> getProfilePicture() {
    return super.getProfilePicture();
  }
}

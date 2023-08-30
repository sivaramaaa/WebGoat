package org.owasp.webgoat.lessons.xmlread;

import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nbaars
 * @since 5/4/17.
 */
@RestController
@RequestMapping("/xmlread/comments")
@AllArgsConstructor
public class CommentsEndpoint {

  private final CommentsCache comments;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Collection<Comment> retrieveComments() {
    return comments.getComments();
  }
}

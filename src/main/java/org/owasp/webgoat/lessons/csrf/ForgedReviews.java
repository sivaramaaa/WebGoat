

package org.owasp.webgoat.lessons.csrf;

import static org.springframework.http.MediaType.ALL_VALUE;

import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForgedReviews extends AssignmentEndpoint {

  @Autowired private WebSession webSession;
  private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");

  private static final Map<String, List<Review>> userReviews = new HashMap<>();
  private static final List<Review> REVIEWS = new ArrayList<>();
  private static final String weakAntiCSRF = "2aa14227b9a13d0bede0388a7fba9aa9";

  static {
    REVIEWS.add(
        new Review("secUriTy", LocalDateTime.now().format(fmt), "This is like swiss cheese", 0));
    REVIEWS.add(new Review("webgoat", LocalDateTime.now().format(fmt), "It works, sorta", 2));
    REVIEWS.add(new Review("guest", LocalDateTime.now().format(fmt), "Best, App, Ever", 5));
    REVIEWS.add(
        new Review(
            "guest",
            LocalDateTime.now().format(fmt),
            "This app is so insecure, I didn't even post this review, can you pull that off too?",
            1));
  }

  @GetMapping(
      path = "/csrf/review",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = ALL_VALUE)
  @ResponseBody
  public Collection<Review> retrieveReviews() {
    Collection<Review> allReviews = Lists.newArrayList();
    Collection<Review> newReviews = userReviews.get(webSession.getUserName());
    if (newReviews != null) {
      allReviews.addAll(newReviews);
    }

    allReviews.addAll(REVIEWS);

    return allReviews;
  }

  @PostMapping("/csrf/review")
  @ResponseBody
  public AttackResult createNewReview(
      String reviewText, Integer stars, String validateReq, HttpServletRequest request) {
    final String host = (request.getHeader("host") == null) ? "NULL" : request.getHeader("host");
    final String referer =
        (request.getHeader("referer") == null) ? "NULL" : request.getHeader("referer");
    final String[] refererArr = referer.split("/");

    Review review = new Review();
    review.setText(reviewText);
    review.setDateTime(LocalDateTime.now().format(fmt));
    review.setUser(webSession.getUserName());
    review.setStars(stars);
    var reviews = userReviews.getOrDefault(webSession.getUserName(), new ArrayList<>());
    reviews.add(review);
    userReviews.put(webSession.getUserName(), reviews);
    // short-circuit
    if (validateReq == null || !validateReq.equals(weakAntiCSRF)) {
      return failed(this).feedback("csrf-you-forgot-something").build();
    }
    // we have the spoofed files
    if (referer != "NULL" && refererArr[2].equals(host)) {
      return failed(this).feedback("csrf-same-host").build();
    } else {
      return success(this)
          .feedback("csrf-review.success")
          .build(); // feedback("xss-stored-comment-failure")
    }
  }
}

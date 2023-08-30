

package org.owasp.webgoat.webwolf.requests;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for fetching all the HTTP requests from WebGoat to WebWolf for a specific user.
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/requests")
public class Requests {

  private final WebWolfTraceRepository traceRepository;
  private final ObjectMapper objectMapper;

  @AllArgsConstructor
  @Getter
  private class Tracert {
    private final Instant date;
    private final String path;
    private final String json;
  }

  @GetMapping
  public ModelAndView get() {
    var model = new ModelAndView("requests");
    var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var traces =
        traceRepository.findAllTraces().stream()
            .filter(t -> allowedTrace(t, user))
            .map(t -> new Tracert(t.getTimestamp(), path(t), toJsonString(t)))
            .collect(toList());
    model.addObject("traces", traces);

    return model;
  }

  private boolean allowedTrace(HttpExchange t, UserDetails user) {
    HttpExchange.Request req = t.getRequest();
    boolean allowed = true;
    /* do not show certain traces to other users in a classroom setup */
    if (req.getUri().getPath().contains("/files")
        && !req.getUri().getPath().contains(user.getUsername())) {
      allowed = false;
    } else if (req.getUri().getPath().contains("/landing")
        && req.getUri().getQuery() != null
        && req.getUri().getQuery().contains("uniqueCode")
        && !req.getUri().getQuery().contains(StringUtils.reverse(user.getUsername()))) {
      allowed = false;
    }

    return allowed;
  }

  private String path(HttpExchange t) {
    return (String) t.getRequest().getUri().getPath();
  }

  private String toJsonString(HttpExchange t) {
    try {
      return objectMapper.writeValueAsString(t);
    } catch (JsonProcessingException e) {
      log.error("Unable to create json", e);
    }
    return "No request(s) found";
  }
}



package org.owasp.webgoat.webwolf.requests;

import com.google.common.collect.EvictingQueue;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;

/**
 * Keep track of all the incoming requests, we are only keeping track of request originating from
 * WebGoat.
 *
 * @author nbaars
 * @since 8/13/17.
 */
@Slf4j
public class WebWolfTraceRepository implements HttpExchangeRepository {

  private final EvictingQueue<HttpExchange> traces = EvictingQueue.create(10000);
  private final List<String> exclusionList =
      List.of(
          "/tmpdir",
          "/home",
          "/files",
          "/images/",
          "/favicon.ico",
          "/js/",
          "/webjars/",
          "/requests",
          "/css/",
          "/mail");

  @Override
  public List<HttpExchange> findAll() {
    return List.of();
  }

  public List<HttpExchange> findAllTraces() {
    return new ArrayList<>(traces);
  }

  private boolean isInExclusionList(String path) {
    return exclusionList.stream().anyMatch(e -> path.contains(e));
  }

  @Override
  public void add(HttpExchange httpTrace) {
    var path = httpTrace.getRequest().getUri().getPath();
    if (!isInExclusionList(path)) {
      traces.add(httpTrace);
    }
  }
}

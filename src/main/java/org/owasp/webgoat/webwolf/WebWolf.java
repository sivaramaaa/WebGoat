

package org.owasp.webgoat.webwolf;

import org.owasp.webgoat.webwolf.requests.WebWolfTraceRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.owasp.webgoat.webwolf")
@PropertySource("classpath:application-webwolf.properties")
@EnableAutoConfiguration
public class WebWolf {

  @Bean
  public HttpExchangeRepository traceRepository() {
    return new WebWolfTraceRepository();
  }
}

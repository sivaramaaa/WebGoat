
package org.owasp.webgoat.container;

import java.io.File;
import org.owasp.webgoat.container.session.UserSessionData;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"org.owasp.webgoat.container", "org.owasp.webgoat.lessons"})
@PropertySource("classpath:application-webgoat.properties")
@EnableAutoConfiguration
public class WebGoat {

  @Bean(name = "pluginTargetDirectory")
  public File pluginTargetDirectory(@Value("${webgoat.user.directory}") final String webgoatHome) {
    return new File(webgoatHome);
  }

  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public WebSession webSession() {
    return new WebSession();
  }

  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public UserSessionData userSessionData() {
    return new UserSessionData("test", "data");
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}

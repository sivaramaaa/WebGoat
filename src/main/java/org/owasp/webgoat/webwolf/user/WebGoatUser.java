

package org.owasp.webgoat.webwolf.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Getter
@Entity
public class WebGoatUser implements UserDetails {

  @Id private String username;
  private String password;
  @Transient private User user;

  protected WebGoatUser() {}

  public WebGoatUser(String username, String password) {
    this.username = username;
    this.password = password;
    createUser();
  }

  public void createUser() {
    this.user = new User(username, password, getAuthorities());
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.user.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.user.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.user.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return this.user.isEnabled();
  }
}

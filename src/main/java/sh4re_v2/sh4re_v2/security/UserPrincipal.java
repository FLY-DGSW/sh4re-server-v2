package sh4re_v2.sh4re_v2.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sh4re_v2.sh4re_v2.domain.User;

@Getter
public class UserPrincipal implements UserDetails {

  private final User user;
  private final Collection<? extends GrantedAuthority> authorities;
  private String nameAttributeKey; // for OAuth
  private Map<String, Object> attributes; // for OAuth

  public UserPrincipal(User user) {
    this.user = user;
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
  }

  public UserPrincipal(User user, Map<String, Object> attributes, String nameAttributeKey) {
    this.user = user;
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getKey()));
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
  }

  /**
   * OAuth2User method implements
   */
//  @Override
//  public String getName() {
//    return user.getId().toString();
//  }

  /**
   * UserDetails method implements
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Long getId() {
    return user.getId();
  }
}

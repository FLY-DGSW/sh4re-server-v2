package sh4re_v2.sh4re_v2.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.security.UserPrincipal;

@Component
public class UserAuthenticationHolder {
  public User current() {
    return ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
  }
}

package sh4re_v2.sh4re_v2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.User;
import sh4re_v2.sh4re_v2.service.UserService;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    return new UserPrincipal(user);
  }

  // This method is used by JwtAuthenticationFilter
  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    User user = userService.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

    return new UserPrincipal(user);
  }
}

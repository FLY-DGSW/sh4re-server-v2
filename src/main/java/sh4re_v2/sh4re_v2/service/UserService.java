package sh4re_v2.sh4re_v2.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.User;
import sh4re_v2.sh4re_v2.dto.getMyInfo.GetMyInfoRes;
import sh4re_v2.sh4re_v2.repository.UserRepository;
import sh4re_v2.sh4re_v2.security.CustomUserDetailService;
import sh4re_v2.sh4re_v2.security.UserPrincipal;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public GetMyInfoRes getMyInfo() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserPrincipal userPrincipal = (UserPrincipal) principal;
    User user = userPrincipal.getUser();
    return new GetMyInfoRes(user);
  }
}

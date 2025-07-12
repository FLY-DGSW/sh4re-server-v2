package sh4re_v2.sh4re_v2.service.main;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.dto.getMyInfo.GetMyInfoRes;
import sh4re_v2.sh4re_v2.exception.error_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.repository.main.UserRepository;

@Service
@Transactional(transactionManager="mainTransactionManager")
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserAuthenticationHolder holder;

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
    User user = holder.current();
    return new GetMyInfoRes(user);
  }

  public void validateUsername(String username) {
    boolean isUsernameExist = findByUsername(username).isPresent();
    if(isUsernameExist) throw AuthException.of(AuthStatusCode.ALREADY_EXISTS_USERNAME);
  }
}

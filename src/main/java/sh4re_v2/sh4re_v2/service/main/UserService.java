package sh4re_v2.sh4re_v2.service.main;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.context.TenantContext;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.dto.user.getMyInfo.GetMyInfoRes;
import sh4re_v2.sh4re_v2.dto.user.getMyInfo.UserWithClassInfo;
import sh4re_v2.sh4re_v2.dto.user.setTheme.SetThemeReq;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.repository.main.UserRepository;
import sh4re_v2.sh4re_v2.service.tenant.ClassPlacementService;

@Service
@Transactional(transactionManager="mainTransactionManager")
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserAuthenticationHolder holder;
  private final ClassPlacementService classPlacementService;

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
    
    TenantContext.setTenantId(user.getSchool().getTenantId());
    
    Optional<ClassPlacement> classPlacement = classPlacementService.findLatestClassPlacementByUserId(user.getId());
    
    UserWithClassInfo userWithClassInfo = UserWithClassInfo.from(user, classPlacement.orElse(null));
    
    return new GetMyInfoRes(userWithClassInfo);
  }

  public void setTheme(SetThemeReq setThemeReq) {
    User user = holder.current();
    user.setTheme(setThemeReq.getThemeEnum());
    this.save(user);
  }

  public void validateUsername(String username) {
    boolean isUsernameExist = findByUsername(username).isPresent();
    if(isUsernameExist) throw AuthException.of(AuthStatusCode.ALREADY_EXISTS_USERNAME);
  }
}

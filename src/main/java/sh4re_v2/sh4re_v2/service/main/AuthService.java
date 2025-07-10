package sh4re_v2.sh4re_v2.service.main;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.dto.login.LoginReq;
import sh4re_v2.sh4re_v2.dto.login.LoginRes;
import sh4re_v2.sh4re_v2.dto.logout.LogOutRes;
import sh4re_v2.sh4re_v2.dto.refreshToken.RefreshTokenRes;
import sh4re_v2.sh4re_v2.dto.register.RegisterReq;
import sh4re_v2.sh4re_v2.dto.register.RegisterRes;
import sh4re_v2.sh4re_v2.exception.error_code.AuthErrorCode;
import sh4re_v2.sh4re_v2.exception.error_code.SchoolErrorCode;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;
import sh4re_v2.sh4re_v2.security.Jwt.JwtTokenProvider;
import sh4re_v2.sh4re_v2.security.TokenStatus;
import sh4re_v2.sh4re_v2.security.UserPrincipal;

@Service
@Transactional(transactionManager="mainTransactionManager")
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private final SchoolService schoolService;
  private final RefreshTokenService refreshTokenService;
  @Value("${cookie.domain:localhost}")
  private String COOKIE_DOMAIN;
  private static final String COOKIE_PATH = "/";
  private static final int COOKIE_MAX_AGE = 60 * 60 * 60 * 24 * 30;

  private Cookie createCookie(String name, String value, boolean isSecure) {
    Cookie cookie = new Cookie(name, value);
    cookie.setDomain(COOKIE_DOMAIN);
    cookie.setPath(COOKIE_PATH);
    cookie.setMaxAge(COOKIE_MAX_AGE);
    cookie.setSecure(isSecure);
    return cookie;
  }

  public LoginRes login(LoginReq loginReq, HttpServletResponse response) {
    try {
      // 인증 시도
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginReq.username(),
              loginReq.password()
          )
      );

      // 인증 성공 시 SecurityContext에 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      User user = userPrincipal.getUser();

      // 토큰 생성
      String accessToken = jwtTokenProvider.generateAccessToken(user);
      String refreshToken = jwtTokenProvider.generateRefreshToken(user);

      refreshTokenService.saveOrUpdateRefreshToken(user.getUsername(), refreshToken);

      // 쿠키 설정
      Cookie refreshCookie = createCookie("refreshToken", refreshToken, true);

      response.addCookie(refreshCookie);

      // 응답 객체 생성 및 반환
      return new LoginRes(accessToken);
    } catch (BadCredentialsException e) {
      // 잘못된 자격 증명 예외 처리
      throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
    } catch (LockedException e) {
      // 계정 잠금 예외 처리
      throw new BusinessException(AuthErrorCode.ACCOUNT_LOCKED);
    } catch (DisabledException e) {
      // 계정 비활성화 예외 처리
      throw new BusinessException(AuthErrorCode.ACCOUNT_DISABLED);
    } catch (Exception e) {
      // 기타 예외 처리
      throw new BusinessException(AuthErrorCode.AUTHENTICATION_FAILED, "인증에 실패했습니다: " + e.getMessage());
    }
  }

  public RegisterRes registerUser(RegisterReq registerReq) {
    Optional<School> schoolOpt = schoolService.findByCode(registerReq.schoolCode());
    if(schoolOpt.isEmpty()) throw SchoolErrorCode.SCHOOL_NOT_FOUND.defaultException();
    // Check if username already exists
    if (userService.findByUsername(registerReq.username()).isPresent()) {
      throw AuthErrorCode.ALREADY_EXISTS_USERNAME.defaultException();
    }

    // Create new user
    String encodedPassword = passwordEncoder.encode(registerReq.password());

    User user = new User(
        registerReq.username(),
        encodedPassword,
        registerReq.email(),
        registerReq.name(),
        registerReq.grade(),
        registerReq.classNo(),
        registerReq.studentNo(),
        schoolOpt.get()
    );

    userService.save(user);

    return new RegisterRes(user.getId());
  }

  public RefreshTokenRes refreshToken(HttpServletRequest request, HttpServletResponse response){
    // 1. 쿠키에서 Refresh Token 꺼내기
    String refreshToken = null;
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals("refreshToken")) {
        refreshToken = cookie.getValue();
      }
    }

    // 2. Refresh Token 검증
    if (refreshToken == null || jwtTokenProvider.validateToken(refreshToken) != TokenStatus.AUTHENTICATED) {
      throw AuthErrorCode.INVALID_JWT.defaultException();
    }

    String username = jwtTokenProvider.extractUsername(refreshToken);
    if (!refreshTokenService.isRefreshTokenValid(username, refreshToken)) {
      throw AuthErrorCode.INVALID_JWT.defaultException();
    }

    // 3. 새 Access Token 발급
    Optional<User> user = userService.findByUsername(username);
    if(user.isEmpty()) throw AuthErrorCode.INVALID_JWT.defaultException();
    String newAccessToken = jwtTokenProvider.generateAccessToken(user.get());

    // 4. 필요시 새 Refresh Token도 발급 & 쿠키에 저장 (옵션)
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.get());
    refreshTokenService.saveOrUpdateRefreshToken(username, newRefreshToken);
    response.addCookie(createCookie("refreshToken", newRefreshToken, true));

    return new RefreshTokenRes(newAccessToken);
  }

  public LogOutRes logout(UserDetails userDetails) {
    refreshTokenService.deleteByUsername(userDetails.getUsername());
    return new LogOutRes();
  }
}

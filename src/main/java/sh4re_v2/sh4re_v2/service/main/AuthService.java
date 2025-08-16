package sh4re_v2.sh4re_v2.service.main;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh4re_v2.sh4re_v2.common.UserAuthenticationHolder;
import sh4re_v2.sh4re_v2.context.TenantContext;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.dto.auth.login.LoginReq;
import sh4re_v2.sh4re_v2.dto.auth.login.LoginRes;
import sh4re_v2.sh4re_v2.dto.auth.refreshToken.RefreshTokenRes;
import sh4re_v2.sh4re_v2.dto.auth.register.RegisterReq;
import sh4re_v2.sh4re_v2.dto.auth.RegisterResponse;
import sh4re_v2.sh4re_v2.dto.auth.resetPassword.ResetPasswordReq;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.SchoolStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.SchoolException;
import sh4re_v2.sh4re_v2.security.jwt.JwtTokenProvider;
import sh4re_v2.sh4re_v2.security.UserPrincipal;
import sh4re_v2.sh4re_v2.service.tenant.ClassPlacementService;

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
  private final ClassPlacementService classPlacementService;
  private final UserAuthenticationHolder holder;
  @Value("${cookie.domain:localhost}")
  private String COOKIE_DOMAIN;
  @Value("${cookie.secure:false}")
  private boolean COOKIE_SECURE;
  @Value("${cookie.same-site:Lax}")
  private String COOKIE_SAME_SITE;
  private static final String COOKIE_PATH = "/";
  private static final int COOKIE_MAX_AGE = 60 * 60 * 60 * 24 * 30;

  private Cookie createCookie(String name, String value) {
    Cookie cookie = new Cookie(name, value);
    cookie.setDomain(COOKIE_DOMAIN);
    cookie.setPath(COOKIE_PATH);
    cookie.setMaxAge(COOKIE_MAX_AGE);
    cookie.setSecure(COOKIE_SECURE);
    cookie.setHttpOnly(true);
    return cookie;
  }

  public LoginRes login(LoginReq loginReq, HttpServletResponse response) {
    // 인증 시도
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginReq.username(),
            loginReq.password()
        )
    );

    // 인증 성공 시 SecurityContext에 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // user 정보 불러오기
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    User user = userPrincipal.getUser();

    // 토큰 생성
    String accessToken = jwtTokenProvider.generateAccessToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);

    // refreshToken 저장
    refreshTokenService.saveOrUpdateRefreshToken(user.getUsername(), refreshToken);

    // 쿠키 설정
    Cookie refreshCookie = createCookie("refreshToken", refreshToken);
    response.addCookie(refreshCookie);

    // 응답 객체 생성 및 반환
    return new LoginRes(accessToken);
  }

  public RegisterResponse registerUser(RegisterReq registerReq) {
    // Check if username already exists
    userService.validateUsername(registerReq.username());

    // Find School. If it doesn't exist, throw an error.
    School school = schoolService
        .findByCode(registerReq.schoolCode())
        .orElseThrow(() -> SchoolException.of(SchoolStatusCode.SCHOOL_NOT_FOUND));

    // Create User Entity
    User user = registerReq.toUserEntity(passwordEncoder, school);

    // Save the user (register)
    userService.save(user);

    // Create ClassPlacement Entity
    ClassPlacement classPlacement = registerReq.toClassPlacement(
        user.getId()
    );

    // Set Tenant ID
    TenantContext.setTenantId(user.getSchool().getTenantId());

    // Save the classPlacement
    classPlacementService.save(classPlacement);

    // return response
    return new RegisterResponse(user.getId());
  }

  public RefreshTokenRes refreshToken(HttpServletRequest request, HttpServletResponse response){
    // 1. 쿠키에서 Refresh Token 꺼내기
    String refreshToken = jwtTokenProvider.getRefreshTokenFromRequest(request);

    // 2. Refresh Token 검증
    jwtTokenProvider.validateRefreshToken(refreshToken);

    // 3. 유저 추출
    String username = jwtTokenProvider.extractUsername(refreshToken);
    User user = userService
        .findByUsername(username)
        .orElseThrow(() -> AuthException.of(AuthStatusCode.INVALID_JWT));

    // 3. 새 Access Token 발급
    String newAccessToken = jwtTokenProvider.generateAccessToken(user);

    // 4. 새 Refresh Token 발급 & 쿠키에 저장
    String newRefreshToken = jwtTokenProvider.generateNewRefreshToken(user);
    response.addCookie(createCookie("refreshToken", newRefreshToken));

    return new RefreshTokenRes(newAccessToken);
  }

  public void logout(UserDetails userDetails) {
    refreshTokenService.deleteByUsername(userDetails.getUsername());
  }

  public void resetPassword(ResetPasswordReq resetPasswordReq) {
    User user = holder.current();
    if(passwordEncoder.matches(resetPasswordReq.password(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(resetPasswordReq.newPassword()));
      userService.save(user);
    } else {
      throw AuthException.of(AuthStatusCode.WRONG_CURRENT_PASSWORD);
    }
  }
}

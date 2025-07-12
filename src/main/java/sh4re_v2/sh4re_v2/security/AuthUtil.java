package sh4re_v2.sh4re_v2.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sh4re_v2.sh4re_v2.context.TenantContext;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.exception.error_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.error_code.SchoolStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;
import sh4re_v2.sh4re_v2.exception.exception.SchoolException;
import sh4re_v2.sh4re_v2.security.jwt.JwtTokenProvider;
import sh4re_v2.sh4re_v2.service.main.SchoolService;

@Component
@RequiredArgsConstructor
public class AuthUtil {
  private final CustomUserDetailService customUserDetailService;
  private final JwtTokenProvider jwtTokenProvider;
  private final SchoolService schoolService;

  public void authenticateUser(HttpServletRequest request) {
    String jwt = extractTokenOrThrow(request);
    validateTokenStatus(jwtTokenProvider.validateToken(jwt));

    String username = jwtTokenProvider.extractUsername(jwt);
    Long schoolId = jwtTokenProvider.extractSchoolId(jwt);
    School school = schoolService.findById(schoolId)
        .orElseThrow(() -> SchoolException.of(SchoolStatusCode.SCHOOL_NOT_FOUND));

    UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(auth);

    TenantContext.setTenantId(school.getTenantId());
  }

  private String extractTokenOrThrow(HttpServletRequest request) {
    String token = jwtTokenProvider.getJwtFromRequest(request);
    if (!StringUtils.hasText(token)) {
      throw AuthException.of(AuthStatusCode.EMPTY_JWT);
    }
    return token;
  }

  private void validateTokenStatus(TokenStatus status) {
    if (status == TokenStatus.EXPIRED) {
      throw AuthException.of(AuthStatusCode.EXPIRED_JWT);
    } else if (status == TokenStatus.INVALID) {
      throw AuthException.of(AuthStatusCode.INVALID_JWT);
    }
  }
}

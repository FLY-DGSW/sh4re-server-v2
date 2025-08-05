package sh4re_v2.sh4re_v2.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sh4re_v2.sh4re_v2.security.AuthUtil;
import sh4re_v2.sh4re_v2.context.TenantContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final AuthUtil authUtil;

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    String token = jwtTokenProvider.getJwtFromRequest(request);
    return token == null;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws IOException {
    try {
      authUtil.authenticateUser(request);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      jwtAuthenticationEntryPoint.commence(request, response,
          new BadCredentialsException("Invalid token", e));
    } finally {
      TenantContext.clear();
    }
  }
}

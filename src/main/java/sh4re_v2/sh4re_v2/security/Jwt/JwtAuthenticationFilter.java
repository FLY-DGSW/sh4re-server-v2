package sh4re_v2.sh4re_v2.security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import sh4re_v2.sh4re_v2.config.SecurityPathConfig;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.exception.error_code.AuthErrorCode;
import sh4re_v2.sh4re_v2.exception.error_code.ErrorCode;
import sh4re_v2.sh4re_v2.exception.error_code.SchoolErrorCode;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;
import sh4re_v2.sh4re_v2.repository.main.SchoolRepository;
import sh4re_v2.sh4re_v2.security.TokenStatus;
import sh4re_v2.sh4re_v2.tenant.SchoolContextHolder;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final SchoolRepository schoolRepository;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String token = getJwtFromRequest(request);

    return token == null;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    try{

    try {
      String jwt = getJwtFromRequest(request);
      if (!StringUtils.hasText(jwt)) {
        setRequestException(request, AuthErrorCode.EMPTY_JWT);
        return;
      }

      TokenStatus tokenStatus = jwtTokenProvider.validateToken(jwt);
      if (tokenStatus == TokenStatus.EXPIRED) {
        setRequestException(request, AuthErrorCode.EXPIRED_JWT);
        return;
      } else if (tokenStatus == TokenStatus.INVALID) {
        setRequestException(request, AuthErrorCode.INVALID_JWT);
        return;
      }

      String username = jwtTokenProvider.extractUsername(jwt);
      Long schoolId = jwtTokenProvider.extractSchoolId(jwt);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      Optional<School> school = schoolRepository.findById(schoolId);
      if(school.isEmpty()) {
        setRequestException(request, SchoolErrorCode.SCHOOL_NOT_FOUND);
        return;
      }
      SchoolContextHolder.setTenantId(school.get().getTenantId());
    } catch (Exception e) {
      log.error("Could not set user authentication in security context", e);
      request.setAttribute("exception", e);
    } finally {
      if(!response.isCommitted() && request.getAttribute("exception") instanceof BusinessException){
        jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("Expired or invalid token", (Throwable) request.getAttribute("exception")));
      } else {
        filterChain.doFilter(request, response);
      }
    }
    } finally {
      SchoolContextHolder.clear();
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private void setRequestException (HttpServletRequest request, ErrorCode authErrorCode){
    request.setAttribute("exception", authErrorCode.defaultException());
  }
}

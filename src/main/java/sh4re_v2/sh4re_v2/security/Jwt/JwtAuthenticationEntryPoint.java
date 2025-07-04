package sh4re_v2.sh4re_v2.security.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.ErrorResponse;
import sh4re_v2.sh4re_v2.exception.error_code.AuthErrorCode;
import sh4re_v2.sh4re_v2.exception.error_code.ErrorCode;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
//    log.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // Get the original exception from the request if available
    Throwable exception = (Throwable) request.getAttribute("exception");
    if (exception == null) {
      exception = authException.getCause();
    }

    ErrorCode errorCode;

    if (exception instanceof BusinessException) {
      errorCode = ((BusinessException) exception).getErrorCode();
    } else if (exception instanceof JwtException
        || authException instanceof BadCredentialsException) {
      errorCode = AuthErrorCode.INVALID_JWT;
    } else {
      errorCode = AuthErrorCode.AUTHENTICATION_FAILED;
    }

//    if (exception != null) {
//      log.error("Authentication failed: {}", exception.getMessage());
//    }

    BusinessException ex = new BusinessException(errorCode, exception);
    ErrorResponse errorResponse = new ErrorResponse(
        errorCode.getCode(),
        ex.getMessage() == null ? errorCode.defaultMessage() : ex.getMessage()
    );
    BaseRes<?> body = new BaseRes<>(false, "에러가 발생했습니다.", errorResponse);

    objectMapper.writeValue(response.getOutputStream(), body);
  }
}

package sh4re_v2.sh4re_v2.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
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
import org.springframework.util.StringUtils;
import sh4re_v2.sh4re_v2.common.HttpRequestEndpointUtil;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.ErrorResponse;
import sh4re_v2.sh4re_v2.exception.error_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.error_code.CommonStatusCode;
import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;
import sh4re_v2.sh4re_v2.exception.exception.ApplicationException;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;
  private final HttpRequestEndpointUtil httpRequestEndpointUtil;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    // 만약 토큰이 없어서 에러가 발생했다면 응답을 설정하고 return
    if(isEmptyJwtThenSetResponse(request, response)) return;
    // 만약 엔드포인트가 없어서 에러가 발생했다면 응답을 설정하고 return
    if(httpRequestEndpointUtil.isInvalidEndpointThenSetResponse(request, response)) return;

    // 반환 타입 설정
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // body 생성
    Throwable exception = authException.getCause() != null ? authException.getCause() : authException;
    BaseRes<?> body = getBody(exception);

    // response에 생성한 body를 직렬화하여 저장한다.
    objectMapper.writeValue(response.getOutputStream(), body);
  }

  private static BaseRes<?> getBody(Throwable exception) {
    StatusCode statusCode;
    if (exception instanceof ApplicationException) {
      statusCode = ((ApplicationException) exception).getStatusCode();
    } else if (
        exception instanceof JwtException ||
        exception instanceof BadCredentialsException ||
        exception instanceof IllegalArgumentException
    ) {
      statusCode = AuthStatusCode.INVALID_JWT;
    } else {
      statusCode = AuthStatusCode.AUTHENTICATION_FAILED;
    }

    ApplicationException ex = new ApplicationException(statusCode, exception);
    return new BaseRes<>(
        false,
        statusCode.getCode(),
        ex.getMessage() == null ? statusCode.getMessage() : ex.getMessage(),
        null
    );
  }

  private boolean isEmptyJwtThenSetResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String token = jwtTokenProvider.getJwtFromRequest(request);
    if (!StringUtils.hasText(token)) {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      BaseRes<?> body = new BaseRes<>(
          false,
          AuthStatusCode.LOGIN_REQUIRED.getCode(),
          AuthStatusCode.LOGIN_REQUIRED.getMessage(),
          null);
      objectMapper.writeValue(response.getOutputStream(), body);
      return true;
    }
    return false;
  }
}

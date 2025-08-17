package sh4re_v2.sh4re_v2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import sh4re_v2.sh4re_v2.dto.ErrorResponse;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    // 반환 타입 설정
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

    // body 생성
    ErrorResponse body = ErrorResponse.of(
        AuthStatusCode.PERMISSION_DENIED.getCode(),
        AuthStatusCode.PERMISSION_DENIED.getMessage()
    );

    // response에 생성한 body를 직렬화하여 저장한다.
    objectMapper.writeValue(response.getOutputStream(), body);
  }
}

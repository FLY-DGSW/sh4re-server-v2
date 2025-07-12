package sh4re_v2.sh4re_v2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import sh4re_v2.sh4re_v2.common.HttpRequestEndpointUtil;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.ErrorResponse;
import sh4re_v2.sh4re_v2.exception.error_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.AuthException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;
  private final HttpRequestEndpointUtil httpRequestEndpointUtil;

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    if(httpRequestEndpointUtil.isInvalidEndpoint(request, response)) return;

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

    BaseRes<?> body = new BaseRes<>(
        false,
        AuthStatusCode.PERMISSION_DENIED.getCode(),
        AuthStatusCode.PERMISSION_DENIED.getMessage(),
        null
    );
    objectMapper.writeValue(response.getOutputStream(), body);
  }
}

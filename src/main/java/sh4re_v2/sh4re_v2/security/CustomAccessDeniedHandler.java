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
import sh4re_v2.sh4re_v2.exception.error_code.AuthErrorCode;
import sh4re_v2.sh4re_v2.exception.error_code.CommonErrorCode;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;
  private final HttpRequestEndpointUtil httpRequestEndpointUtil;

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    if(!httpRequestEndpointUtil.isEndpointExistOrElseSetErrorResponse(request, response)) return;

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

    BaseRes<?> body = new BaseRes<>(false, "에러가 발생했습니다.", new ErrorResponse(AuthErrorCode.PERMISSION_DENIED.getCode(), AuthErrorCode.PERMISSION_DENIED.getMessage()));
    objectMapper.writeValue(response.getOutputStream(), body);
  }
}

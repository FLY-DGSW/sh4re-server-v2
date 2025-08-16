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
import sh4re_v2.sh4re_v2.common.HttpRequestEndpointUtil;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;
  private final HttpRequestEndpointUtil httpRequestEndpointUtil;

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    // 만약 엔드포인트가 없어서 에러가 발생했다면 응답을 설정하고 return
    if(httpRequestEndpointUtil.isInvalidEndpointThenSetResponse(request, response)) return;

    // 반환 타입 설정
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

    // body 생성
    BaseRes<?> body = new BaseRes<>(
        AuthStatusCode.PERMISSION_DENIED.getCode(),
        AuthStatusCode.PERMISSION_DENIED.getMessage(),
        null
    );

    // response에 생성한 body를 직렬화하여 저장한다.
    objectMapper.writeValue(response.getOutputStream(), body);
  }
}

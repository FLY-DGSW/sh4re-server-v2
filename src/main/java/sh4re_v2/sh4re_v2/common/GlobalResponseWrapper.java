package sh4re_v2.sh4re_v2.common;

import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.ApiResponseError;
import sh4re_v2.sh4re_v2.exception.ErrorResponse;

@ControllerAdvice(basePackages = "sh4re_v2.sh4re_v2")
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    if (returnType.getContainingClass().equals(OpenApiResource.class)) {
      return false;
    }
    return true; // 모든 컨트롤러에 적용
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof BaseRes<?>) {
      return body; // 이미 감싸진 경우 pass
    }
    if(body instanceof ErrorResponse || body instanceof ApiResponseError) {
      return new BaseRes<>(false, "에러가 발생했습니다.", body);
    }
    return new BaseRes<>(true, "성공", body);
  }
}


package sh4re_v2.sh4re_v2.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import sh4re_v2.sh4re_v2.config.SecurityPathConfig;
import sh4re_v2.sh4re_v2.config.SecurityPathConfig.EndpointConfig;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.status_code.CommonStatusCode;

@Component
@RequiredArgsConstructor
public class HttpRequestEndpointUtil {
  private final ObjectMapper objectMapper;
  private final DispatcherServlet servlet;

  public boolean isInvalidEndpointThenSetResponse(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if(!isEndpointExist(request)){
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      BaseRes<?> body = new BaseRes<>(
          false,
          CommonStatusCode.ENDPOINT_NOT_FOUND.getCode(),
          CommonStatusCode.ENDPOINT_NOT_FOUND.getMessage(),
          null);
      objectMapper.writeValue(response.getOutputStream(), body);
      return true;
    }
    return false;
  }

  private boolean isEndpointExist(HttpServletRequest request) {
    EndpointConfig[] endpointConfigs = SecurityPathConfig.endpointConfigs;
    for (EndpointConfig config : endpointConfigs) {
      try {
        boolean isMatch;
        if(config.getMethod() != null) {
          isMatch = PathPatternRequestMatcher.withDefaults()
              .matcher(
                  config.getMethod(),
                  config.getPattern())
              .matches(request);
        } else {
          isMatch = PathPatternRequestMatcher.withDefaults()
              .matcher(
                  config.getPattern()
              )
              .matches(request);
        }
        if (isMatch) {
          return true;
        }
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }
}

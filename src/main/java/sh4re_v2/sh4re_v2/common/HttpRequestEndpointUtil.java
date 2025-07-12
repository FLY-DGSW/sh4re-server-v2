package sh4re_v2.sh4re_v2.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.ErrorResponse;
import sh4re_v2.sh4re_v2.exception.error_code.CommonStatusCode;

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
    List<HandlerMapping> handlerMappings = servlet.getHandlerMappings();
    if(handlerMappings == null) return false;
    handlerMappings = handlerMappings.subList(0, handlerMappings.size() - 1);
    for (HandlerMapping handlerMapping : handlerMappings) {
      try {
        HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
        if (foundHandler != null) {
          return true;
        }
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }
}

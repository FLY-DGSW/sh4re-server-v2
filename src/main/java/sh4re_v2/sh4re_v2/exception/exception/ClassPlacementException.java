package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;

@Getter
public class ClassPlacementException extends ApplicationException {
  public ClassPlacementException(StatusCode statusCode) {
    super(statusCode);
  }

  public ClassPlacementException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public ClassPlacementException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }
}

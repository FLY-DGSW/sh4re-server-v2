package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;

@Getter
public class AuthException extends ApplicationException {
  public AuthException(StatusCode statusCode) {
    super(statusCode);
  }

  public AuthException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public AuthException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }
}

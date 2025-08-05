package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;

@Getter
public class UserException extends ApplicationException {
  public UserException(StatusCode statusCode) {
    super(statusCode);
  }

  public UserException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public UserException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }
}

package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;

@Getter
public class SchoolException extends ApplicationException {
  public SchoolException(StatusCode statusCode) {
    super(statusCode);
  }

  public SchoolException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public SchoolException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }
}

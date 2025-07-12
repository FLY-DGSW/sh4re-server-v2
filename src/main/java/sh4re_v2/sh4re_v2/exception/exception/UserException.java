package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;

@Getter
public class UserException extends RuntimeException {
  private final StatusCode statusCode;

  public UserException(StatusCode statusCode) {
    super(statusCode.getMessage());
    this.statusCode = statusCode;
  }

  public UserException(StatusCode statusCode, Throwable cause) {
    super(statusCode.getMessage(), cause);
    this.statusCode = statusCode;
  }

  public UserException(StatusCode statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public static UserException of(StatusCode statusCode) {
    return new UserException(statusCode);
  }

  public static UserException of(StatusCode statusCode, Throwable cause) {
    return new UserException(statusCode, cause);
  }

  public static UserException of(StatusCode statusCode, String customMessage) {
    return new UserException(statusCode, customMessage);
  }
}

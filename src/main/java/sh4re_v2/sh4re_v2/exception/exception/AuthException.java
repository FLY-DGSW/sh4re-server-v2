package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;

@Getter
public class AuthException extends RuntimeException {
  private final StatusCode statusCode;

  public AuthException(StatusCode statusCode) {
    super(statusCode.getMessage());
    this.statusCode = statusCode;
  }

  public AuthException(StatusCode statusCode, Throwable cause) {
    super(statusCode.getMessage(), cause);
    this.statusCode = statusCode;
  }

  public AuthException(StatusCode statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public static AuthException of(StatusCode statusCode) {
    return new AuthException(statusCode);
  }

  public static AuthException of(StatusCode statusCode, Throwable cause) {
    return new AuthException(statusCode, cause);
  }

  public static AuthException of(StatusCode statusCode, String customMessage) {
    return new AuthException(statusCode, customMessage);
  }
}

package sh4re_v2.sh4re_v2.exception.exception;

import lombok.Getter;
import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;

@Getter
public class SchoolException extends RuntimeException {
  private final StatusCode statusCode;

  public SchoolException(StatusCode statusCode) {
    super(statusCode.getMessage());
    this.statusCode = statusCode;
  }

  public SchoolException(StatusCode statusCode, Throwable cause) {
    super(statusCode.getMessage(), cause);
    this.statusCode = statusCode;
  }

  public SchoolException(StatusCode statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public static SchoolException of(StatusCode statusCode) {
    return new SchoolException(statusCode);
  }

  public static SchoolException of(StatusCode statusCode, Throwable cause) {
    return new SchoolException(statusCode, cause);
  }

  public static SchoolException of(StatusCode statusCode, String customMessage) {
    return new SchoolException(statusCode, customMessage);
  }
}

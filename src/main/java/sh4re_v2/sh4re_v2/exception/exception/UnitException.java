package sh4re_v2.sh4re_v2.exception.exception;

import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;

public class UnitException extends ApplicationException {

  public UnitException(StatusCode statusCode) {
    super(statusCode);
  }

  public UnitException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public UnitException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }

  public static UnitException of(StatusCode statusCode) {
    return new UnitException(statusCode);
  }

  public static UnitException of(StatusCode statusCode, Throwable cause) {
    return new UnitException(statusCode, cause);
  }

  public static UnitException of(StatusCode statusCode, String customMessage) {
    return new UnitException(statusCode, customMessage);
  }
}
package sh4re_v2.sh4re_v2.exception.exception;

import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;

public class AssignmentException extends ApplicationException {

  public AssignmentException(StatusCode statusCode) {
    super(statusCode);
  }

  public AssignmentException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public AssignmentException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }

  public static AssignmentException of(StatusCode statusCode) {
    return new AssignmentException(statusCode);
  }

  public static AssignmentException of(StatusCode statusCode, Throwable cause) {
    return new AssignmentException(statusCode, cause);
  }

  public static AssignmentException of(StatusCode statusCode, String customMessage) {
    return new AssignmentException(statusCode, customMessage);
  }
}
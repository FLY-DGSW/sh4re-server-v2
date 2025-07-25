package sh4re_v2.sh4re_v2.exception.exception;

import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;

public class SubjectException extends ApplicationException {

  public SubjectException(StatusCode statusCode) {
    super(statusCode);
  }

  public SubjectException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public SubjectException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }
}

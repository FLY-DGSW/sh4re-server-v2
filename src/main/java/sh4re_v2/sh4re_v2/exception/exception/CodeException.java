package sh4re_v2.sh4re_v2.exception.exception;

import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;

public class CodeException extends ApplicationException {
  public CodeException(StatusCode statusCode) {
    super(statusCode);
  }

  public static CodeException of(StatusCode statusCode) {
    return new CodeException(statusCode);
  }
}
package sh4re_v2.sh4re_v2.exception.error_code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String getCode();
  String defaultMessage();
  HttpStatus defaultHttpStatus();
  Exception defaultException();
  Exception defaultException(Throwable cause);
  Exception defaultException(String message);
}
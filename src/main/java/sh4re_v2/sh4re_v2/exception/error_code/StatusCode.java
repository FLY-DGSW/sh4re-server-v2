package sh4re_v2.sh4re_v2.exception.error_code;

import org.springframework.http.HttpStatus;

public interface StatusCode {
  String getCode();
  String getMessage();
  HttpStatus getHttpStatus();
}
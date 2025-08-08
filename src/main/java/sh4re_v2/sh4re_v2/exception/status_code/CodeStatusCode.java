package sh4re_v2.sh4re_v2.exception.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CodeStatusCode implements StatusCode {
  CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "CODE_001", "코드를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String statusCode;
  private final String message;

  @Override
  public String getCode() {
    return statusCode;
  }
}
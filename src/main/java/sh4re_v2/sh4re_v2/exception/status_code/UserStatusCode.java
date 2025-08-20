package sh4re_v2.sh4re_v2.exception.status_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode {
  USER_NOT_FOUND("USER_NOT_FOUND", "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
}
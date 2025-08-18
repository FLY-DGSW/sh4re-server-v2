package sh4re_v2.sh4re_v2.exception.status_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UnitStatusCode implements StatusCode {
  UNIT_NOT_FOUND("UNIT_NOT_FOUND", "존재하지 않는 단원입니다.", HttpStatus.NOT_FOUND),;

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
}
package sh4re_v2.sh4re_v2.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sh4re_v2.sh4re_v2.exception.exception.ApplicationException;

@Getter
@RequiredArgsConstructor
public enum SchoolStatusCode implements StatusCode {
  SCHOOL_NOT_FOUND("SCHOOL_NOT_FOUND", "존재하지 않는 학교 코드입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
}

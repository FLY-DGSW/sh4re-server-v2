package sh4re_v2.sh4re_v2.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClassPlacementStatusCode implements StatusCode {
  CLASS_PLACEMENT_NOT_FOUND("CLASS_PLACEMENT_NOT_FOUND", "사용자의 학적 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),;

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
}

package sh4re_v2.sh4re_v2.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  INVALID_ARGUMENT("INVALID_ARGUMENT", "유효하지 않은 인자입니다.", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  ENDPOINT_NOT_FOUND("ENDPOINT_NOT_FOUND", "존재하지 않는 엔드포인트입니다.", HttpStatus.NOT_FOUND),;

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  @Override
  public String defaultMessage() {
    return message;
  }

  @Override
  public HttpStatus defaultHttpStatus() {
    return httpStatus;
  }

  @Override
  public RuntimeException defaultException() {
    return new BusinessException(this);
  }

  @Override
  public RuntimeException defaultException(Throwable cause) {
    return new BusinessException(this, cause);
  }

  @Override
  public Exception defaultException(String message) {
    return null;
  }
}

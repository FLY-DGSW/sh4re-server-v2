package sh4re_v2.sh4re_v2.exception.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
  INVALID_JWT("INVALID_TOKEN", "유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
  EXPIRED_JWT("EXPIRED_TOKEN", "만료된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
  UNSUPPORTED_JWT("UNSUPPORTED_JWT", "지원되지 않는 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
  EMPTY_JWT("EMPTY_JWT", "JWT 토큰이 비어있습니다.", HttpStatus.UNAUTHORIZED),
  JWT_CLAIMS_EMPTY("JWT_CLAIMS_EMPTY", "JWT 클레임이 비어있습니다.", HttpStatus.UNAUTHORIZED),
  ACCOUNT_LOCKED("ACCOUNT_LOCKED", "계정이 잠겨 있습니다.", HttpStatus.UNAUTHORIZED),
  ACCOUNT_DISABLED("ACCOUNT_DISABLED", "계정이 비활성화되었습니다.", HttpStatus.UNAUTHORIZED),
  INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  ALREADY_EXISTS_USERNAME("ALREADY_EXISTS_USERNAME", "해당 아이디를 가진 유저가 이미 존재합니다.", HttpStatus.BAD_REQUEST),
  ALREADY_LOGGED_OUT_USER("ALREADY_LOGGED_OUT_USER", "이미 로그아웃된 계정입니다.", HttpStatus.BAD_REQUEST),
  AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", "인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
  SCHOOL_NOT_FOUND("SCHOOL_NOT_FOUND", "존재하지 않는 학교 코드입니다.", HttpStatus.BAD_REQUEST),;

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
    return new BusinessException(this, message);
  }
}

package sh4re_v2.sh4re_v2.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sh4re_v2.sh4re_v2.dto.ErrorResponse;
import sh4re_v2.sh4re_v2.exception.status_code.AuthStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.CommonStatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.StatusCode;
import sh4re_v2.sh4re_v2.exception.status_code.TenantStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.ApplicationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
    StatusCode statusCode = ex.getStatusCode();
    ErrorResponse error = ErrorResponse.of(
        statusCode.getCode(),
        ex.getMessage() != null ? ex.getMessage() : statusCode.getMessage()
    );
    return ResponseEntity
        .status(statusCode.getHttpStatus())
        .body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> details = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String field = ((FieldError) error).getField();
      String message = error.getDefaultMessage() != null ? error.getDefaultMessage() : "잘못된 입력값 입니다.";
      details.put(field, message);
    });

    ErrorResponse error = ErrorResponse.of(
        CommonStatusCode.INVALID_ARGUMENT.getCode(),
        "요청값이 유효하지 않습니다.",
        details
    );

    return ResponseEntity
        .status(CommonStatusCode.INVALID_ARGUMENT.getHttpStatus())
        .body(error);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
    if (ex.getMessage().contains("No tenant identifier")) {
      ErrorResponse error = ErrorResponse.of(
          TenantStatusCode.TENANT_NOT_FOUND.getCode(),
          TenantStatusCode.TENANT_NOT_FOUND.getMessage()
      );
      return ResponseEntity
          .status(TenantStatusCode.TENANT_NOT_FOUND.getHttpStatus())
          .body(error);
    }
    ErrorResponse error = ErrorResponse.of(
        CommonStatusCode.INTERNAL_SERVER_ERROR.getCode(),
        CommonStatusCode.INTERNAL_SERVER_ERROR.getMessage()
    );
    return ResponseEntity
        .status(CommonStatusCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .body(error);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
    ErrorResponse error = ErrorResponse.of(
        AuthStatusCode.INVALID_CREDENTIALS.getCode(),
        AuthStatusCode.INVALID_CREDENTIALS.getMessage()
    );
    return ResponseEntity
        .status(AuthStatusCode.INVALID_CREDENTIALS.getHttpStatus())
        .body(error);
  }

  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ErrorResponse> handleAccountLockedException(LockedException ex) {
    ErrorResponse error = ErrorResponse.of(
        AuthStatusCode.ACCOUNT_LOCKED.getCode(),
        AuthStatusCode.ACCOUNT_LOCKED.getMessage()
    );
    return ResponseEntity
        .status(AuthStatusCode.ACCOUNT_LOCKED.getHttpStatus())
        .body(error);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ErrorResponse> handleAccountDisabledException(DisabledException ex) {
    ErrorResponse error = ErrorResponse.of(
        AuthStatusCode.ACCOUNT_DISABLED.getCode(),
        AuthStatusCode.ACCOUNT_DISABLED.getMessage()
    );
    return ResponseEntity
        .status(AuthStatusCode.ACCOUNT_DISABLED.getHttpStatus())
        .body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("요청 처리 중 에러 발생: {}", ex.getMessage());
    ErrorResponse error = ErrorResponse.of(
        CommonStatusCode.INTERNAL_SERVER_ERROR.getCode(),
        CommonStatusCode.INTERNAL_SERVER_ERROR.getMessage()
    );
    return ResponseEntity
        .status(CommonStatusCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .body(error);
  }
}

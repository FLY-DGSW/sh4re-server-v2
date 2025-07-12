package sh4re_v2.sh4re_v2.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sh4re_v2.sh4re_v2.dto.BaseRes;
import sh4re_v2.sh4re_v2.exception.error_code.CommonStatusCode;
import sh4re_v2.sh4re_v2.exception.error_code.StatusCode;
import sh4re_v2.sh4re_v2.exception.error_code.TenantStatusCode;
import sh4re_v2.sh4re_v2.exception.exception.ApplicationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<BaseRes<Void>> handleApplicationException(ApplicationException ex) {
    StatusCode statusCode = ex.getStatusCode();
    return ResponseEntity
        .status(statusCode.getHttpStatus())
        .body(new BaseRes<>(
            false,
            statusCode.getCode(),
            ex.getMessage() != null ? ex.getMessage() : statusCode.getMessage(),
            null
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseRes<List<ApiSimpleError>>> handleValidationException(MethodArgumentNotValidException ex) {
    List<ApiSimpleError> errors = ex.getBindingResult().getAllErrors().stream()
        .map(error -> {
          String field = ((FieldError) error).getField();
          String message = error.getDefaultMessage() != null ? error.getDefaultMessage() : "잘못된 입력값 입니다.";
          return ApiSimpleError.builder().field(field).message(message).build();
        })
        .toList();

    return ResponseEntity
        .status(CommonStatusCode.INVALID_ARGUMENT.getHttpStatus())
        .body(new BaseRes<>(
            false,
            CommonStatusCode.INVALID_ARGUMENT.getCode(),
            "요청값이 유효하지 않습니다.",
            errors
        ));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<BaseRes<Void>> handleIllegalState(Exception e) {
    if (e.getMessage().contains("No tenant identifier")) {
      return ResponseEntity
          .status(TenantStatusCode.TENANT_NOT_FOUND.getHttpStatus())
          .body(new BaseRes<>(
              false,
              TenantStatusCode.TENANT_NOT_FOUND.getCode(),
              TenantStatusCode.TENANT_NOT_FOUND.getMessage(),
              null
          ));
    }
    return ResponseEntity
        .status(CommonStatusCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .body(new BaseRes<>(
            false,
            CommonStatusCode.INTERNAL_SERVER_ERROR.getCode(),
            CommonStatusCode.INTERNAL_SERVER_ERROR.getMessage(),
            null
        ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseRes<Void>> handleException(Exception ex) {
    log.error("요청 처리 중 에러 발생: {}", ex.getMessage());
    return ResponseEntity
        .status(CommonStatusCode.INTERNAL_SERVER_ERROR.getHttpStatus())
        .body(new BaseRes<>(
            false,
            CommonStatusCode.INTERNAL_SERVER_ERROR.getCode(),
            CommonStatusCode.INTERNAL_SERVER_ERROR.getMessage(),
            null
        ));
  }
}

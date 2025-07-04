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
import sh4re_v2.sh4re_v2.exception.error_code.CommonErrorCode;
import sh4re_v2.sh4re_v2.exception.error_code.ErrorCode;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    ErrorResponse errorResponse = new ErrorResponse(
        errorCode.getCode(),
        ex.getMessage() == null ? errorCode.defaultMessage() : ex.getMessage()
    );
    return ResponseEntity.status(errorCode.defaultHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponseError> handleValidationException(MethodArgumentNotValidException ex) {
    List<ApiSimpleError> errors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      assert errorMessage != null;
      errors.add(ApiSimpleError.builder().field(fieldName).message(errorMessage).build());
    });

    ApiResponseError response = ApiResponseError.builder()
        .code(CommonErrorCode.INVALID_ARGUMENT.getCode())
        .status(CommonErrorCode.INVALID_ARGUMENT.defaultHttpStatus().value())
        .name("ValidationException")
        .message("요청값이 유효하지 않습니다")
        .cause(errors)
        .build();

    return ResponseEntity.status(CommonErrorCode.INVALID_ARGUMENT.defaultHttpStatus()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("요청 처리 중 에러 발생: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(
        CommonErrorCode.INTERNAL_SERVER_ERROR.getCode(),
        CommonErrorCode.INTERNAL_SERVER_ERROR.defaultMessage()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}

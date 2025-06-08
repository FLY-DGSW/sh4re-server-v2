package sh4re_v2.sh4re_v2.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import sh4re_v2.sh4re_v2.exception.error_code.ErrorCode;
import sh4re_v2.sh4re_v2.exception.exception.BusinessException;

/**
 *
 * @param code 에러 코드 명
 * @param status 상태 코드 값
 * @param name 오류 이름
 * @param message 오류 메시지
 * @param cause
 * @param timestamp 발생 시각
 */
@Builder
public record ApiResponseError(
    String code,
    @JsonIgnore
    Integer status,
    @JsonIgnore
    String name,
    String message,
    @JsonInclude(Include.NON_EMPTY) List<ApiSimpleError> cause,
    @JsonIgnore
    Instant timestamp
) {
  public static ApiResponseError of(BusinessException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    String errorName = exception.getClass().getName();
    errorName = errorName.substring(errorName.lastIndexOf('.') + 1);

    return ApiResponseError.builder()
        .code(errorCode.getCode())
        .status(errorCode.defaultHttpStatus().value())
        .name(errorName)
        .message(exception.getMessage())
        .cause(ApiSimpleError.listOfCauseSimpleError(exception.getCause()))
        .build();
  }

  public ApiResponseError {
    if (code == null) {
      code = "API_ERROR";
    }

    if (status == null) {
      status = 500;
    }

    if (name == null) {
      name = "ApiError";
    }

    if (message == null || message.isBlank()) {
      message = "API 오류";
    }

    if (timestamp == null) {
      timestamp = Instant.now();
    }
  }
}
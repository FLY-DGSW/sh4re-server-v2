package sh4re_v2.sh4re_v2.common;

import java.util.Map;
import sh4re_v2.sh4re_v2.dto.ErrorResponse;

public record ApiResponse<T>(
    T data,
    ErrorResponse error
) {
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(data, null);
  }

  public static ApiResponse<Void> error(String code, String message) {
    return new ApiResponse<>(null, ErrorResponse.of(code, message));
  }

  public static ApiResponse<Void> error(String code, String message, Map<String, String> details) {
    return new ApiResponse<>(null, ErrorResponse.of(code, message, details));
  }

  public static ApiResponse<Void> error(ErrorResponse error) {
    return new ApiResponse<>(null, error);
  }
}


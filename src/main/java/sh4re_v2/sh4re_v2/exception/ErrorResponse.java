package sh4re_v2.sh4re_v2.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
  private String code;
  private String message;

  public ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
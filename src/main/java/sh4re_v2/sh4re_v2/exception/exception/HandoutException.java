package sh4re_v2.sh4re_v2.exception.exception;

import sh4re_v2.sh4re_v2.exception.status_code.HandoutStatusCode;

public class HandoutException extends ApplicationException {
  private HandoutException(HandoutStatusCode statusCode) {
    super(statusCode);
  }

  public static HandoutException of(HandoutStatusCode statusCode) {
    return new HandoutException(statusCode);
  }
}
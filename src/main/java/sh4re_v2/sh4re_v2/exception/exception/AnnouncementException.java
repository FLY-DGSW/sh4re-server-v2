package sh4re_v2.sh4re_v2.exception.exception;

import sh4re_v2.sh4re_v2.exception.status_code.AnnouncementStatusCode;

public class AnnouncementException extends ApplicationException {
  private AnnouncementException(AnnouncementStatusCode statusCode) {
    super(statusCode);
  }

  public static AnnouncementException of(AnnouncementStatusCode statusCode) {
    return new AnnouncementException(statusCode);
  }
}
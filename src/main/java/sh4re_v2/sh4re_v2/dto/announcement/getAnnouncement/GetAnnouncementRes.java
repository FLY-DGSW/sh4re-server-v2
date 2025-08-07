package sh4re_v2.sh4re_v2.dto.announcement.getAnnouncement;

import java.time.format.DateTimeFormatter;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;

public record GetAnnouncementRes(
    Long id,
    String label,
    String title,
    String author,
    String content,
    String createdAt
) {
  public static GetAnnouncementRes from(Announcement announcement) {
    return new GetAnnouncementRes(
        announcement.getId(),
        announcement.getLabel(),
        announcement.getTitle(),
        announcement.getAuthor(),
        announcement.getContent(),
        announcement.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
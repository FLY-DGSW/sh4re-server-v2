package sh4re_v2.sh4re_v2.dto.announcement.getAllAnnouncements;

import java.time.format.DateTimeFormatter;
import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;

public record GetAllAnnouncementsRes(
    List<AnnouncementDto> announcements
) {
  public static GetAllAnnouncementsRes from(List<Announcement> announcements) {
    return new GetAllAnnouncementsRes(announcements.stream().map(AnnouncementDto::from).toList());
  }
}

record AnnouncementDto(
    Long id,
    String label,
    String title,
    String author,
    String content,
    String createdAt
) {
  public static AnnouncementDto from(Announcement announcement) {
    return new AnnouncementDto(
        announcement.getId(),
        announcement.getLabel(),
        announcement.getTitle(),
        announcement.getAuthor(),
        announcement.getContent(),
        announcement.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}

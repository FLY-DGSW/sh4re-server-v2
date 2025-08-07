package sh4re_v2.sh4re_v2.dto.announcement.updateAnnouncement;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;
import sh4re_v2.sh4re_v2.validation.annotation.ValidEnum;

public record UpdateAnnouncementReq(
    @NotBlank(message = "라벨은 필수 입력값 입니다.")
    @ValidEnum(enumClass = AnnouncementLabel.class, message = "라벨은 '공지' 또는 '과제'만 가능합니다.")
    String label,

    @NotBlank(message = "제목은 필수 입력값 입니다.")
    @Length(min = 1, message = "제목은 3자 이상이여야 합니다.")
    String title,

    @NotBlank(message = "작성자는 필수 입력값 입니다.")
    String author,

    @NotBlank(message = "내용은 필수 입력값 입니다.")
    String content
) {
  public Announcement toEntity(Announcement existing) {
    existing.setLabel(this.label);
    existing.setTitle(this.title);
    existing.setAuthor(this.author);
    existing.setContent(this.content);
    return existing;
  }

  public enum AnnouncementLabel {
    공지, 과제
  }
}
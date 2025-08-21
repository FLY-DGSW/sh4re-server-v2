package sh4re_v2.sh4re_v2.dto.announcement.createAnnouncement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.domain.tenant.Announcement;
import sh4re_v2.sh4re_v2.dto.announcement.Target;
import sh4re_v2.sh4re_v2.validation.annotation.ValidEnum;

public record CreateAnnouncementReq(
    @NotBlank(message = "라벨은 필수 입력값 입니다.")
    @ValidEnum(enumClass = AnnouncementLabel.class, message = "라벨은 '공지' 또는 '과제'만 가능합니다.")
    String label,

    @NotBlank(message = "제목은 필수 입력값 입니다.")
    @Length(min = 3, message = "제목은 3자 이상이여야 합니다.")
    String title,

    @NotBlank(message = "작성자는 필수 입력값 입니다.")
    String author,

    @NotBlank(message = "내용은 필수 입력값 입니다.")
    String content,

    @Valid
    @NotNull(message = "대상은 필수 입력값 입니다.")
    Target target
) {
  public Announcement toEntity(Long authorId) {
    return Announcement
        .builder()
        .label(this.label())
        .title(this.title)
        .author(this.author)
        .content(this.content)
        .grade(this.target.grade())
        .schoolYear(this.target.year())
        .classNumber(this.target.classNumber())
        .authorId(authorId)
        .build();
  }

  public enum AnnouncementLabel {
    공지, 과제
  }
}
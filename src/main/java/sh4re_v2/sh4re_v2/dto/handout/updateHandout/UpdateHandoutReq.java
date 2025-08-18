package sh4re_v2.sh4re_v2.dto.handout.updateHandout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;

public record UpdateHandoutReq(
    @NotNull(message = "ID는 필수 입력값 입니다.")
    Long id,

    @NotBlank(message = "제목은 필수 입력값 입니다.")
    @Length(min = 3, message = "제목은 3자 이상이여야 합니다.")
    String title,

    @NotBlank(message = "설명은 필수 입력값 입니다.")
    String description,

    @NotBlank(message = "파일 URL은 필수 입력값 입니다.")
    String fileUrl,

    @NotNull(message = "과목 ID는 필수 입력값 입니다.")
    Long subjectId,

    Long unitId
) {
  public Handout toEntity(Handout existingHandout) {
    existingHandout.setTitle(this.title);
    existingHandout.setDescription(this.description);
    existingHandout.setFileUrl(this.fileUrl);
    return existingHandout;
  }
}
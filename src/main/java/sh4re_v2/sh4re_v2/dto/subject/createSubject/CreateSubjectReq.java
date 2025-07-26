package sh4re_v2.sh4re_v2.dto.subject.createSubject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

public record CreateSubjectReq(
    @NotBlank(message = "과목명은 필수 입력값 입니다.")
    @Length(min = 2, max = 20, message = "과목명은 2자 이상 20자 이하여야 합니다.")
    String name,

    @NotBlank(message = "과목 설명은 필수 입력값 입니다.")
    String description,

    @NotNull(message = "학년은 필수 입력값 입니다.")
    @Max(value = 3, message = "학년은 1~3학년만 입력할 수 있습니다.")
    @Min(value = 1, message = "학년은 1~3학년만 입력할 수 있습니다.")
    int grade,

    @NotNull(message = "반은 필수 입력값 입니다.")
    @Max(value = 100, message = "반은 1~100학년만 입력할 수 있습니다.")
    @Min(value = 1, message = "반은 1~100반만 입력할 수 있습니다.")
    int classNumber,

    @NotNull(message = "연도는 필수 입력값 입니다.")
    int year
) {
  public Subject toEntity(Long userId) {
    return Subject
        .builder()
        .name(this.name())
        .description(this.description)
        .grade(this.grade)
        .schoolYear(this.year)
        .classNumber(this.classNumber)
        .userId(userId)
        .build();
  }
}

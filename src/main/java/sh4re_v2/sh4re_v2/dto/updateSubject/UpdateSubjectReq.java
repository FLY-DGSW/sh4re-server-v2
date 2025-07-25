package sh4re_v2.sh4re_v2.dto.updateSubject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateSubjectReq(
    @NotNull
    Long id,

    @Length(min = 2, max = 20, message = "과목명은 2자 이상 20자 이하여야 합니다.")
    String name,

    String description,

    @Max(value = 3, message = "학년은 1~3학년만 입력할 수 있습니다.")
    @Min(value = 1, message = "학년은 1~3학년만 입력할 수 있습니다.")
    Integer grade,

    @Max(value = 100, message = "반은 1~100학년만 입력할 수 있습니다.")
    @Min(value = 1, message = "반은 1~100반만 입력할 수 있습니다.")
    Integer classNumber,

    Integer year
) {
  public Subject toEntity(Subject subject) {
    if(this.name() != null) subject.setName(this.name());
    if(this.description() != null) subject.setDescription(this.description());
    if(this.grade() != null) subject.setGrade(this.grade());
    if(this.classNumber() != null) subject.setClassNumber(this.classNumber());
    if(this.year() != null) subject.setSchoolYear(this.year());
    return subject;
  }
}

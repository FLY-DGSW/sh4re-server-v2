package sh4re_v2.sh4re_v2.dto.code.createCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

public record CreateCodeReq(
    @NotBlank String title,
    @NotBlank String student,
    @NotBlank String language,
    String description,
    @NotBlank String code,
    @NotBlank String className,
    String assignment,
    @NotNull Integer schoolYear
) {
  public Code toEntity(Long userId) {
    return Code.builder()
        .title(title)
        .student(student)
        .language(language)
        .description(description)
        .code(code)
        .className(className)
        .assignment(assignment)
        .schoolYear(schoolYear)
        .userId(userId)
        .build();
  }
}
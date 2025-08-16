package sh4re_v2.sh4re_v2.dto.code.createCode;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

public record CreateCodeReq(
    @NotBlank String title,
    @NotBlank String language,
    String description,
    @NotBlank String code,
    @NotBlank String className,
    String assignment,
    Boolean useAiDescription
) {
  public Code toEntity(Long userId, String studentName) {
    return Code.builder()
        .title(title)
        .student(studentName)
        .language(language)
        .description(description)
        .code(code)
        .className(className)
        .assignment(assignment)
        .schoolYear(LocalDate.now().getYear())
        .userId(userId)
        .build();
  }
  
  public Code toEntityWithDescription(Long userId, String studentName, String finalDescription) {
    return Code.builder()
        .title(title)
        .student(studentName)
        .language(language)
        .description(finalDescription)
        .code(code)
        .className(className)
        .assignment(assignment)
        .schoolYear(LocalDate.now().getYear())
        .userId(userId)
        .build();
  }
}
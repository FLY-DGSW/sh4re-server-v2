package sh4re_v2.sh4re_v2.dto.code.createCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
public record CreateCodeReq(
    @NotBlank String title,
    @NotBlank String language,
    String description,
    @NotBlank String code,
    @NotNull Long classPlacementId,
    Long assignmentId,
    Boolean useAiDescription
) {
  public Code toEntity(Long authorId, ClassPlacement classPlacement, Assignment assignment) {
    return toEntityWithDescription(authorId, classPlacement, assignment, this.description);
  }
  
  public Code toEntityWithDescription(Long authorId, ClassPlacement classPlacement, Assignment assignment, String finalDescription) {
    return Code.builder()
        .title(title)
        .language(language)
        .description(finalDescription)
        .code(code)
        .classPlacement(classPlacement)
        .assignment(assignment)
        .schoolYear(LocalDate.now().getYear())
        .authorId(authorId)
        .build();
  }
}
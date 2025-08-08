package sh4re_v2.sh4re_v2.dto.code.updateCode;

import jakarta.validation.constraints.NotBlank;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

public record UpdateCodeReq(
    @NotBlank String title,
    @NotBlank String language,
    String description,
    @NotBlank String code,
    @NotBlank String className,
    String assignment
) {
  public Code toEntity(Code existingCode) {
    existingCode.setTitle(title);
    existingCode.setLanguage(language);
    existingCode.setDescription(description);
    existingCode.setCode(code);
    existingCode.setClassName(className);
    existingCode.setAssignment(assignment);
    return existingCode;
  }
}
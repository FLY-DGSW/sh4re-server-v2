package sh4re_v2.sh4re_v2.dto.code.updateCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

public record UpdateCodeReq(
    @NotBlank String title,
    @NotBlank String student,
    @NotBlank String language,
    @NotBlank String description,
    @NotBlank String code,
    @NotBlank String className,
    @NotBlank String assignment,
    @NotNull Integer schoolYear
) {
  public Code toEntity(Code existingCode) {
    existingCode.setTitle(title);
    existingCode.setStudent(student);
    existingCode.setLanguage(language);
    existingCode.setDescription(description);
    existingCode.setCode(code);
    existingCode.setClassName(className);
    existingCode.setAssignment(assignment);
    existingCode.setSchoolYear(schoolYear);
    return existingCode;
  }
}
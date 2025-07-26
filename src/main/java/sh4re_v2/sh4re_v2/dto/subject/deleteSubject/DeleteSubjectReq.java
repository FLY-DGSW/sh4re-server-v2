package sh4re_v2.sh4re_v2.dto.subject.deleteSubject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

public record DeleteSubjectReq(
    @NotNull(message = "과목 ID는 필수 입력 값입니다.")
    Long id
) {}

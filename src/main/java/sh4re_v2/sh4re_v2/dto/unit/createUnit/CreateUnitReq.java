package sh4re_v2.sh4re_v2.dto.unit.createUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUnitReq(
    @NotNull(message = "과목 ID는 필수 입력값입니다.")
    Long subjectId,

    @NotBlank(message = "단원 제목은 필수 입력값입니다.")
    String title,

    String description,

    @NotNull(message = "순서는 필수 입력값입니다.")
    Integer orderIndex
) {}
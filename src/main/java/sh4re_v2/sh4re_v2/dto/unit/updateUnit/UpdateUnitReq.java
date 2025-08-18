package sh4re_v2.sh4re_v2.dto.unit.updateUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUnitReq(
    @NotNull(message = "단원 ID는 필수 입력값입니다.")
    Long id,

    @NotBlank(message = "단원 제목은 필수 입력값입니다.")
    String title,

    String description,

    @NotNull(message = "순서는 필수 입력값입니다.")
    Integer orderIndex
) {}
package sh4re_v2.sh4re_v2.dto.unit.deleteUnit;

import jakarta.validation.constraints.NotNull;

public record DeleteUnitReq(
    @NotNull(message = "단원 ID는 필수 입력값입니다.")
    Long id
) {}
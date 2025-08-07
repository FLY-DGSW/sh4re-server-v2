package sh4re_v2.sh4re_v2.dto.announcement;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record Target(
    @NotNull(message = "연도는 필수 입력값 입니다.")
    Integer year,

    @NotNull(message = "학년은 필수 입력값 입니다.")
    @Max(value = 3, message = "학년은 1~3학년만 입력할 수 있습니다.")
    @Min(value = 1, message = "학년은 1~3학년만 입력할 수 있습니다.")
    Integer grade,

    @NotNull(message = "반은 필수 입력값 입니다.")
    @Max(value = 100, message = "반은 1~100반만 입력할 수 있습니다.")
    @Min(value = 1, message = "반은 1~100반만 입력할 수 있습니다.")
    Integer classNumber
) {}
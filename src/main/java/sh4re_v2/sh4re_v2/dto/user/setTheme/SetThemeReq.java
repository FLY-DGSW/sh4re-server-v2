package sh4re_v2.sh4re_v2.dto.user.setTheme;

import jakarta.validation.constraints.NotNull;
import sh4re_v2.sh4re_v2.common.Theme;
import sh4re_v2.sh4re_v2.validation.annotation.ValidEnum;

public record SetThemeReq(
    @NotNull(message = "테마 이름은 필수 입력 값입니다.")
    @ValidEnum(enumClass = Theme.class)
    String themeName
) {
    public Theme getThemeEnum() {
        return Theme.valueOf(themeName.toUpperCase());
    }
}

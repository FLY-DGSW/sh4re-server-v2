package sh4re_v2.sh4re_v2.dto.login;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginReq(
    @NotBlank
    @Length(message = "username은 4~16글자 사이여야 합니다.", min = 4, max = 16)
    String username,
    @NotBlank
    @Length(message = "password는 8~24글자 사이여야 합니다.", min = 8, max = 24)
    String password
) {}

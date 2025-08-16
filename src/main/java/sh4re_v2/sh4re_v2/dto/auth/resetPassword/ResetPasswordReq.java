package sh4re_v2.sh4re_v2.dto.auth.resetPassword;

import jakarta.validation.constraints.NotBlank;
import sh4re_v2.sh4re_v2.validation.annotation.ValidPassword;

public record ResetPasswordReq(
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @ValidPassword
    String password,

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @ValidPassword
    String newPassword
) {}

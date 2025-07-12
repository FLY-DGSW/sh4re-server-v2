package sh4re_v2.sh4re_v2.dto.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import sh4re_v2.sh4re_v2.validation.annotation.ValidPassword;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginReq(
    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Length(min = 4, max = 12, message = "아이디는 4자 이상 12자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 소문자 영어와 숫자만 포함해야 합니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @ValidPassword
    String password
) {}

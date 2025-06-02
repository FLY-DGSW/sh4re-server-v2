package sh4re_v2.sh4re_v2.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterReq(
  @NotBlank(message = "아이디는 필수 입력값입니다.")
  @Length(message = "username은 4~16글자 사이여야 합니다.", min = 4, max = 16)
  String username,
  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Length(message = "password는 8~24글자 사이여야 합니다.", min = 8, max = 24)
  String password,
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  @Email(message = "올바른 이메일을 입력해 주세요.")
  String email,
  @NotBlank(message = "이름은 필수 입력값입니다.")
  String name,
  @NotNull(message = "학년은 필수 입력값입니다.")
  Integer grade,
  @NotNull(message = "반은 필수 입력값입니다.")
  Integer classNo,
  @NotNull(message = "번호는 필수 입력값입니다.")
  Integer studentNo
) {}

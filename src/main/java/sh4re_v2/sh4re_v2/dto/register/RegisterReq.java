package sh4re_v2.sh4re_v2.dto.register;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;
import sh4re_v2.sh4re_v2.domain.main.School;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.validation.annotation.ValidPassword;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterReq(
  @NotBlank(message = "아이디는 필수 입력 항목입니다.")
  @Length(min = 4, max = 12, message = "아이디는 4자 이상 12자 이하여야 합니다.")
  @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 소문자 영어와 숫자만 포함해야 합니다.")
  String username,

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  @ValidPassword
  String password,

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
      message = "유효한 이메일 주소를 입력해주세요."
  )
  String email,

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  @Length(min = 2, max = 10, message = "이름은 2자 이상 10자 이하여야 합니다.")
  @Pattern(regexp = "^[a-zA-Z가-힣0-9]+$", message = "이름은 한글, 영어, 숫자만 포함할 수 있습니다.")
  @NotBlank
  String name,

  @NotNull(message = "학년는 필수 입력 항목입니다.")
  @Min(value = 1, message = "학년은 1 이상이어야 합니다.")
  @Max(value = 3, message = "학년은 3 이하여야 합니다.")
  int grade,

  @NotNull(message = "반 번호는 필수 입력 항목입니다.")
  @Min(value = 1, message = "반 번호는 1 이상이어야 합니다.")
  @Max(value = 99, message = "반 번호는 99 이하여야 합니다.")
  int classNumber,

  @NotNull(message = "번호는 필수 입력 항목입니다.")
  @Min(value = 1, message = "번호는 1 이상이어야 합니다.")
  @Max(value = 99, message = "번호는 99 이하여야 합니다.")
  int studentNumber,

  @NotBlank(message = "학교 코드는 필수 입력값입니다.")
  String schoolCode
) {
  public User toUserEntity(PasswordEncoder passwordEncoder, School school) {

    return new User(
        username(),
        passwordEncoder.encode(password()),
        email(),
        name(),
        school
    );
  }

  public ClassPlacement toClassPlacement(
      Long userId
  ) {
    return new ClassPlacement(
        userId,
        grade(),
        classNumber(),
        studentNumber()
    );
  }
}

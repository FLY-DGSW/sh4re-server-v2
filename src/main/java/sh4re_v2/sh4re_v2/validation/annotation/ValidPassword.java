package sh4re_v2.sh4re_v2.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import sh4re_v2.sh4re_v2.validation.validator.PasswordValidator;

@Documented
@Constraint(validatedBy = PasswordValidator.class)  // ★ 여기서 실제 검사 클래스 지정
@Target({ ElementType.FIELD, ElementType.PARAMETER })  // 어디에 붙일 수 있나: 필드 or 파라미터
@Retention(RetentionPolicy.RUNTIME)  // 런타임까지 유지됨
public @interface ValidPassword {
  String message() default "비밀번호는 영어 대소문자, 숫자, 특수문자를 포함하고 8~24자여야 합니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
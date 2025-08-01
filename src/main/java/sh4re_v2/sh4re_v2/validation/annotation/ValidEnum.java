package sh4re_v2.sh4re_v2.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import sh4re_v2.sh4re_v2.validation.validator.EnumValidator;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
  String message() default "잘못된 값입니다. 정확한 값을 입력해 주세요.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  Class<? extends java.lang.Enum<?>> enumClass();
}
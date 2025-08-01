package sh4re_v2.sh4re_v2.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sh4re_v2.sh4re_v2.validation.annotation.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
  private ValidEnum annotation;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Return true for null values, let @NotNull handle null validation
    if (value == null) {
      return true;
    }

    boolean result = false;
    Object[] enumValues = this.annotation.enumClass().getEnumConstants();
    if (enumValues != null) {
      for (Object enumValue : enumValues) {
        if (value.equalsIgnoreCase(enumValue.toString())) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}

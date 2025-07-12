package sh4re_v2.sh4re_v2.validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sh4re_v2.sh4re_v2.validation.annotation.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

  private static final String PASSWORD_REGEX =
      "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|;:'\",.<>?/]).{8,24}$";

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password == null || password.isBlank()) return false;
    return password.matches(PASSWORD_REGEX);
  }
}
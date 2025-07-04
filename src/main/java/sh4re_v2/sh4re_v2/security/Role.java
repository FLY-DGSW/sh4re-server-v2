package sh4re_v2.sh4re_v2.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  NOT_REGISTERED("ROLE_NOT_REGISTERED", "회원가입 이전 사용자"),
  USER("ROLE_USER", "일반 사용자"),
  TEACHER("ROLE_TEACHER", "선생님"),
  ADMIN("ROLE_ADMIN", "관리자");

  private final String key;
  private final String title;
}
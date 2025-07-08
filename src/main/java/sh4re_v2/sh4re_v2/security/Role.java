package sh4re_v2.sh4re_v2.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  STUDENT("ROLE_STUDENT", "학생"),
  TEACHER("ROLE_TEACHER", "선생님"),
  ADMIN("ROLE_ADMIN", "관리자");

  private final String authority;
  private final String name;
}
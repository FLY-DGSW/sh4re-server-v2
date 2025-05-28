package sh4re_v2.sh4re_v2.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenStatus {
  AUTHENTICATED,
  EXPIRED,
  INVALID
}

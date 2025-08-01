package sh4re_v2.sh4re_v2.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Theme {
  DARK("dark"),
  WHITE("white");

  private final String name;
}

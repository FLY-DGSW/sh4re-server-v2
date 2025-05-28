package sh4re_v2.sh4re_v2.dto;

public record BaseRes<T> (
  boolean ok,
  String message,
  T data
) {}

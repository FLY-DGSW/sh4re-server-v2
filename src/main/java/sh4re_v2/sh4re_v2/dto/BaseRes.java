package sh4re_v2.sh4re_v2.dto;

public record BaseRes<T> (
  boolean ok,
  String code,
  String message,
  T data
) {

  public BaseRes(boolean ok, String message, T data) {
    this(ok, "SUCCESS", message, data);
  }
}

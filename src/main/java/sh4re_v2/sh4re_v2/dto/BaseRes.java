package sh4re_v2.sh4re_v2.dto;

public record BaseRes<T> (
  String code,
  String message,
  T data
) {

  public BaseRes(boolean ok, String message, T data) {
    this("SUCCESS", message, data);
  }
}

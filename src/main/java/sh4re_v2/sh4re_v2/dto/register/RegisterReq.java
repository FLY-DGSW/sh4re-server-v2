package sh4re_v2.sh4re_v2.dto.register;

public record RegisterReq(
  String username,
  String password,
  String email,
  String name,
  int grade,
  int classNo,
  int studentNo
) {}

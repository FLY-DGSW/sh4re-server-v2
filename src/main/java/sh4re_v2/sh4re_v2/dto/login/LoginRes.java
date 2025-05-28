package sh4re_v2.sh4re_v2.dto.login;

public record LoginRes(
    String accessToken,
    String refreshToken,
    Long userId,
    String username
) {}
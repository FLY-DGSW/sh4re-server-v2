package sh4re_v2.sh4re_v2.dto.getMyInfo;

import sh4re_v2.sh4re_v2.domain.main.User;

public record GetMyInfoRes(
    User me
) {}
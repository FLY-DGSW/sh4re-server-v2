package sh4re_v2.sh4re_v2.dto.code.getCode;

import java.time.format.DateTimeFormatter;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

public record GetCodeRes(
    Long id,
    String title,
    String student,
    String language,
    String description,
    String code,
    Long likeCount,
    boolean isLikedByUser,
    String className,
    String assignment,
    String createdAt,
    String updatedAt
) {
  public static GetCodeRes from(Code code, Long likeCount, boolean isLikedByUser) {
    return new GetCodeRes(
        code.getId(),
        code.getTitle(),
        code.getStudent(),
        code.getLanguage(),
        code.getDescription(),
        code.getCode(),
        likeCount,
        isLikedByUser,
        code.getClassName(),
        code.getAssignment(),
        code.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        code.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
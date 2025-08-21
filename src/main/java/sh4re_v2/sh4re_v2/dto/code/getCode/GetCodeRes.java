package sh4re_v2.sh4re_v2.dto.code.getCode;

import java.time.format.DateTimeFormatter;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.domain.main.User;

public record GetCodeRes(
    Long id,
    String title,
    User author,
    String language,
    String description,
    String code,
    Long likeCount,
    boolean isLikedByUser,
    ClassPlacement classInfo,
    String assignmentTitle,
    String createdAt,
    String updatedAt
) {
  public static GetCodeRes from(Code code, Long likeCount, boolean isLikedByUser, User author, ClassPlacement classInfo) {
    String assignmentTitle = code.getAssignment() != null 
        ? code.getAssignment().getTitle() 
        : "";
        
    return new GetCodeRes(
        code.getId(),
        code.getTitle(),
        author,
        code.getLanguage(),
        code.getDescription(),
        code.getCode(),
        likeCount,
        isLikedByUser,
        classInfo,
        assignmentTitle,
        code.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        code.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
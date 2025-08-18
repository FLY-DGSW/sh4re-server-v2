package sh4re_v2.sh4re_v2.dto.code.getCode;

import java.time.format.DateTimeFormatter;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.service.main.UserService;
import sh4re_v2.sh4re_v2.domain.main.User;

public record GetCodeRes(
    Long id,
    String title,
    String authorName,
    String language,
    String description,
    String code,
    Long likeCount,
    boolean isLikedByUser,
    String className,
    String assignmentTitle,
    String createdAt,
    String updatedAt
) {
  public static GetCodeRes from(Code code, Long likeCount, boolean isLikedByUser, UserService userService) {
    String className = code.getClassPlacement() != null 
        ? code.getClassPlacement().getGrade() + "학년 " + code.getClassPlacement().getClassNumber() + "반"
        : "";
    
    String assignmentTitle = code.getAssignment() != null 
        ? code.getAssignment().getTitle() 
        : "";
    
    String authorName = userService.findById(code.getAuthorId())
        .map(User::getName)
        .orElse("Unknown");
        
    return new GetCodeRes(
        code.getId(),
        code.getTitle(),
        authorName,
        code.getLanguage(),
        code.getDescription(),
        code.getCode(),
        likeCount,
        isLikedByUser,
        className,
        assignmentTitle,
        code.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        code.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
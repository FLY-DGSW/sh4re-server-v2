package sh4re_v2.sh4re_v2.dto.code.getAllCodes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.service.main.UserService;
import sh4re_v2.sh4re_v2.domain.main.User;

public record GetAllCodesRes(
    List<CodeDto> codes
) {
  public static GetAllCodesRes from(List<Code> codes, java.util.function.Function<Long, Long> getLikeCount, UserService userService) {
    return new GetAllCodesRes(codes.stream().map(code -> CodeDto.from(code, getLikeCount.apply(code.getId()), userService)).toList());
  }
}

record CodeDto(
    Long id,
    String title,
    String authorName,
    String language,
    String code,
    Long likeCount,
    String className,
    String assignmentTitle,
    String createdAt,
    String updatedAt
) {
  public static CodeDto from(Code code, Long likeCount, UserService userService) {
    String className = code.getClassPlacement() != null 
        ? code.getClassPlacement().getGrade() + "학년 " + code.getClassPlacement().getClassNumber() + "반"
        : "";
    
    String assignmentTitle = code.getAssignment() != null 
        ? code.getAssignment().getTitle() 
        : "";
    
    String authorName = userService.findById(code.getAuthorId())
        .map(User::getName)
        .orElse("Unknown");
        
    return new CodeDto(
        code.getId(),
        code.getTitle(),
        authorName,
        code.getLanguage(),
        code.getCode(),
        likeCount,
        className,
        assignmentTitle,
        code.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        code.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
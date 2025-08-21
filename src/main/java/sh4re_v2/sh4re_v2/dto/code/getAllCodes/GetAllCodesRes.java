package sh4re_v2.sh4re_v2.dto.code.getAllCodes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Code;
import sh4re_v2.sh4re_v2.domain.tenant.ClassPlacement;
import sh4re_v2.sh4re_v2.domain.main.User;

public record GetAllCodesRes(
    List<CodeDto> codes
) {
  public static GetAllCodesRes from(List<CodeWithDetails> codeDetails) {
    return new GetAllCodesRes(codeDetails.stream().map(CodeDto::from).toList());
  }
  
  public record CodeWithDetails(
      Code code,
      Long likeCount,
      User author,
      ClassPlacement classInfo
  ) {}
}

record CodeDto(
    Long id,
    String title,
    User author,
    ClassPlacement classInfo,
    String language,
    String code,
    Long likeCount,
    String assignmentTitle,
    String createdAt,
    String updatedAt
) {
  public static CodeDto from(GetAllCodesRes.CodeWithDetails details) {
    Code code = details.code();
    String assignmentTitle = code.getAssignment() != null 
        ? code.getAssignment().getTitle() 
        : "";

    return new CodeDto(
        code.getId(),
        code.getTitle(),
        details.author(),
        details.classInfo(),
        code.getLanguage(),
        code.getCode(),
        details.likeCount(),
        assignmentTitle,
        code.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        code.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
package sh4re_v2.sh4re_v2.dto.code.getAllCodes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Code;

public record GetAllCodesRes(
    List<CodeDto> codes
) {
  public static GetAllCodesRes from(List<Code> codes, java.util.function.Function<Long, Long> getLikeCount) {
    return new GetAllCodesRes(codes.stream().map(code -> CodeDto.from(code, getLikeCount.apply(code.getId()))).toList());
  }
}

record CodeDto(
    Long id,
    String title,
    String student,
    String language,
    String code,
    Long likeCount,
    String className,
    String assignment,
    String createdAt,
    String updatedAt
) {
  public static CodeDto from(Code code, Long likeCount) {
    return new CodeDto(
        code.getId(),
        code.getTitle(),
        code.getStudent(),
        code.getLanguage(),
        code.getCode(),
        likeCount,
        code.getClassName(),
        code.getAssignment(),
        code.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        code.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );
  }
}
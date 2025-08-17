package sh4re_v2.sh4re_v2.dto.handout.getHandout;

import java.time.LocalDateTime;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;

public record GetHandoutRes(
    Long id,
    String title,
    String description,
    String author,
    String fileUrl,
    Long subjectId,
    Long userId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
  public static GetHandoutRes from(Handout handout) {
    return new GetHandoutRes(
        handout.getId(),
        handout.getTitle(),
        handout.getDescription(),
        handout.getAuthor(),
        handout.getFileUrl(),
        handout.getSubjectId(),
        handout.getUserId(),
        handout.getCreatedAt(),
        handout.getUpdatedAt()
    );
  }
}
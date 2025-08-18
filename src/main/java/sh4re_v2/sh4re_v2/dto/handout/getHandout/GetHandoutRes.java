package sh4re_v2.sh4re_v2.dto.handout.getHandout;

import java.time.LocalDateTime;
import sh4re_v2.sh4re_v2.domain.main.User;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

public record GetHandoutRes(
    Long id,
    String title,
    String description,
    User author,
    String fileUrl,
    Subject subject,
    Long authorId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
  public static GetHandoutRes from(Handout handout, User author) {
    return new GetHandoutRes(
        handout.getId(),
        handout.getTitle(),
        handout.getDescription(),
        author,
        handout.getFileUrl(),
        handout.getSubject(),
        handout.getAuthorId(),
        handout.getCreatedAt(),
        handout.getUpdatedAt()
    );
  }
}
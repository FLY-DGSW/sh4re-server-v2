package sh4re_v2.sh4re_v2.dto.assignment.createAssignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateAssignmentReq(
    @NotNull(message = "과목 ID는 필수 입력값입니다.")
    Long subjectId,

    @NotBlank(message = "과제 제목은 필수 입력값입니다.")
    String title,

    @NotBlank(message = "과제 설명은 필수 입력값입니다.")
    String description,

    String inputExample,

    String outputExample,

    @NotNull(message = "마감 기한은 필수 입력값입니다.")
    LocalDateTime deadline,

    Long unitId
) {}
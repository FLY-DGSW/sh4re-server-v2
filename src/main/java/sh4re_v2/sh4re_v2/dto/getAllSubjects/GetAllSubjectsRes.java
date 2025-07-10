package sh4re_v2.sh4re_v2.dto.getAllSubjects;

import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Subject;

public record GetAllSubjectsRes(
    List<Subject> subjects
) {}

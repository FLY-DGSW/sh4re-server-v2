package sh4re_v2.sh4re_v2.dto.assignment.getAllAssignments;

import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
import java.util.List;

public record GetAllAssignmentsRes(List<Assignment> assignments) {}
package sh4re_v2.sh4re_v2.dto.assignment.getAllAssignments;

import java.util.Map;
import java.util.stream.Collectors;
import sh4re_v2.sh4re_v2.domain.tenant.Assignment;
import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;

public record GetAllAssignmentsRes(
    List<AssignmentsByUnit> assignments
) {
  public static GetAllAssignmentsRes from(List<Assignment> assignments) {
    Map<Unit, List<Assignment>> groupedByUnit = assignments.stream()
        .collect(Collectors.groupingBy(Assignment::getUnit));

    List<AssignmentsByUnit> assignmentsPerUnit = groupedByUnit.entrySet().stream()
        .map(entry -> new AssignmentsByUnit(entry.getKey(), entry.getValue()))
        .sorted(java.util.Comparator.comparing(a -> a.unit().getOrderIndex()))
        .toList();
    return new GetAllAssignmentsRes(assignmentsPerUnit);
  }
  

  public record AssignmentsByUnit(
      Unit unit,
      List<Assignment> assignments
  ) {}
}
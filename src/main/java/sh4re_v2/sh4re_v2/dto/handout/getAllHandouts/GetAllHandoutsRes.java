package sh4re_v2.sh4re_v2.dto.handout.getAllHandouts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.domain.tenant.Unit;

public record GetAllHandoutsRes(
    List<HandoutsByUnit> handouts
) {
  public static GetAllHandoutsRes from(List<Handout> handouts) {
    Map<Unit, List<Handout>> groupedByUnit = handouts.stream()
        .collect(Collectors.groupingBy(Handout::getUnit));

    List<HandoutsByUnit> handoutsPerUnit = groupedByUnit.entrySet().stream()
        .map(entry -> new HandoutsByUnit(entry.getKey(), entry.getValue()))
        .toList();
    return new GetAllHandoutsRes(handoutsPerUnit);
  }

  public record HandoutsByUnit(
      Unit unit,
      List<Handout> handouts
  ) {}
}
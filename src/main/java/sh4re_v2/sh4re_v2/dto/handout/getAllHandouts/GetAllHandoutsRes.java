package sh4re_v2.sh4re_v2.dto.handout.getAllHandouts;

import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;

public record GetAllHandoutsRes(
    List<Handout> handouts
) {
  public static GetAllHandoutsRes from(List<Handout> handouts) {
    return new GetAllHandoutsRes(handouts);
  }
}
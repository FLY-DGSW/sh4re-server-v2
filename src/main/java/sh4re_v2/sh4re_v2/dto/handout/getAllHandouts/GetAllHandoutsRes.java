package sh4re_v2.sh4re_v2.dto.handout.getAllHandouts;

import java.util.List;
import sh4re_v2.sh4re_v2.domain.tenant.Handout;
import sh4re_v2.sh4re_v2.dto.handout.getHandout.GetHandoutRes;

public record GetAllHandoutsRes(
    List<GetHandoutRes> handouts
) {
  public static GetAllHandoutsRes from(List<Handout> handouts) {
    List<GetHandoutRes> handoutRes = handouts.stream()
        .map(GetHandoutRes::from)
        .toList();
    return new GetAllHandoutsRes(handoutRes);
  }
}
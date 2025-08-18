package sh4re_v2.sh4re_v2.dto.unit.getAllUnits;

import sh4re_v2.sh4re_v2.domain.tenant.Unit;
import java.util.List;

public record GetAllUnitsRes(List<Unit> units) {}
package sh4re_v2.sh4re_v2.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import sh4re_v2.sh4re_v2.context.TenantContext;

public class SchoolRoutingDataSource extends AbstractRoutingDataSource {
  @Override
  protected Object determineCurrentLookupKey() {
    String tenantId = TenantContext.getTenantId();

    if (tenantId == null) {
      throw new IllegalStateException("[🛑 오류] 현재 요청에 schoolId가 설정되지 않았습니다.");
    }

    return tenantId;
  }
}

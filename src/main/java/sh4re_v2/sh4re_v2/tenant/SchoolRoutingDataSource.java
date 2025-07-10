package sh4re_v2.sh4re_v2.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class SchoolRoutingDataSource extends AbstractRoutingDataSource {
  @Override
  protected Object determineCurrentLookupKey() {
    String tenantId = SchoolContextHolder.getTenantId();

    if (tenantId == null) {
      throw new IllegalStateException("[🛑 오류] 현재 요청에 schoolId가 설정되지 않았습니다.");
    }

    return tenantId;
  }
}

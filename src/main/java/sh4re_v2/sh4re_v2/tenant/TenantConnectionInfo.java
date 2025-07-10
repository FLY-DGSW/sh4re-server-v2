package sh4re_v2.sh4re_v2.tenant;

import lombok.Getter;

@Getter
public class TenantConnectionInfo {
  private final String tenantId;
  private final String dbUrl;
  private final String dbUsername;
  private final String dbPassword;

  public TenantConnectionInfo(String tenantId, String dbUrl, String dbUsername, String dbPassword) {
    this.tenantId = tenantId;
    this.dbUrl = dbUrl;
    this.dbUsername = dbUsername;
    this.dbPassword = dbPassword;
  }

  @Override
  public String toString() {
    return "TenantConnectionInfo{" +
        "tenantId='" + tenantId + '\'' +
        ", dbUrl='" + dbUrl + '\'' +
        ", dbUsername='" + dbUsername + '\'' +
        '}';
  }
}

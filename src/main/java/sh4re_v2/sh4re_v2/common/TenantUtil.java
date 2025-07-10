package sh4re_v2.sh4re_v2.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sh4re_v2.sh4re_v2.tenant.TenantConnectionInfo;

@Component
public class TenantUtil {
  @Value("${spring.datasource.main.url}")
  private String mainDbUrl;

  @Value("${spring.datasource.main.username}")
  private String mainDbUsername;

  @Value("${spring.datasource.main.password}")
  private String mainDbPassword;

  public List<TenantConnectionInfo> loadTenants() throws SQLException {
    Connection conn = DriverManager.getConnection(mainDbUrl, mainDbUsername, mainDbPassword);
    ResultSet rs = conn.createStatement().executeQuery("SELECT tenant_id, db_url, db_username, db_password FROM school");

    List<TenantConnectionInfo> tenants = new ArrayList<>();
    while (rs.next()) {
      tenants.add(new TenantConnectionInfo(
          rs.getString("tenant_id"),
          rs.getString("db_url"),
          rs.getString("db_username"),
          rs.getString("db_password")
      ));
    }
    return tenants;
  }
}

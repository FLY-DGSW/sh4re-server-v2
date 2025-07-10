package sh4re_v2.sh4re_v2.flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sh4re_v2.sh4re_v2.common.TenantUtil;
import sh4re_v2.sh4re_v2.tenant.TenantConnectionInfo;

@Component
@RequiredArgsConstructor
public class FlywayTenantMigration implements ApplicationRunner {
  private final TenantUtil tenantUtil;

  @Override
  public void run(ApplicationArguments args) throws SQLException {
    List<TenantConnectionInfo> tenants = tenantUtil.loadTenants(); // school1, school2, ...
    for (TenantConnectionInfo tenant : tenants) {
      if(!hasBeenMigrated(tenant)) {
        Flyway flyway = Flyway.configure()
            .dataSource(tenant.getDbUrl(), tenant.getDbUsername(), tenant.getDbPassword())
            .locations("classpath:db/migration/tenant")
            .baselineOnMigrate(true)
            .load();
        flyway.migrate();
      }
    }
  }

  private boolean hasBeenMigrated(TenantConnectionInfo tenant) {
    String query = "SELECT version FROM flyway_schema_history LIMIT 1";

    try (
        Connection conn = DriverManager.getConnection(
            tenant.getDbUrl(),
            tenant.getDbUsername(),
            tenant.getDbPassword()
        );
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()
    ) {
      return rs.next();
    } catch (SQLException e) {
      return false;
    }
  }
}


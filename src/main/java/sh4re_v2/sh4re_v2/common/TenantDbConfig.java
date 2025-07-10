package sh4re_v2.sh4re_v2.common;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import sh4re_v2.sh4re_v2.tenant.SchoolRoutingDataSource;
import sh4re_v2.sh4re_v2.tenant.TenantConnectionInfo;

@Configuration
@EnableJpaRepositories(
    basePackages = "sh4re_v2.sh4re_v2.repository.tenant",
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager"
)
@RequiredArgsConstructor
public class TenantDbConfig {
  private final TenantUtil tenantUtil;

  @Bean
  public DataSource tenantDataSource() throws SQLException {
    SchoolRoutingDataSource routingDataSource = new SchoolRoutingDataSource();

    Map<Object, Object> targetDataSources = new HashMap<>();
    List<TenantConnectionInfo> tenants = tenantUtil.loadTenants(); // school1, school2, ...

    for (TenantConnectionInfo tenantInfo : tenants) {
      targetDataSources.put(tenantInfo.getTenantId(), buildDs(tenantInfo.getDbUrl(), tenantInfo.getDbUsername(), tenantInfo.getDbPassword()));
    }

    routingDataSource.setTargetDataSources(targetDataSources);
    return routingDataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
      @Qualifier("tenantDataSource") DataSource ds) {
    return createEntityManagerFactory(ds, "sh4re_v2.sh4re_v2.domain.tenant");
  }

  @Bean
  public PlatformTransactionManager tenantTransactionManager(
      @Qualifier("tenantEntityManagerFactory") EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  private DataSource buildDs(String url, String username, String password) {
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(url);
    ds.setUsername(username);
    ds.setPassword(password);
    return ds;
  }

  private LocalContainerEntityManagerFactoryBean createEntityManagerFactory(DataSource ds, String pkg) {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(ds);
    emf.setPackagesToScan(pkg);
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    emf.setJpaPropertyMap(Map.of(
        "hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
    ));

    Properties props = new Properties();
    props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    emf.setJpaProperties(props);

    return emf;
  }
}
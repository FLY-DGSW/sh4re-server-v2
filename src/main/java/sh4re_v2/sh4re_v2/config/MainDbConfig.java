package sh4re_v2.sh4re_v2.config;

import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "sh4re_v2.sh4re_v2.repository.main",
    entityManagerFactoryRef = "mainEntityManagerFactory",
    transactionManagerRef = "mainTransactionManager"
)
public class MainDbConfig {

  @Value("${spring.datasource.main.url}")
  private String mainDbUrl;

  @Value("${spring.datasource.main.username}")
  private String mainDbUsername;

  @Value("${spring.datasource.main.password}")
  private String mainDbPassword;

  @Bean
  public DataSource mainDataSource() {
    return DataSourceBuilder.create()
        .url(mainDbUrl)
        .username(mainDbUsername)
        .password(mainDbPassword)
        .build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(
      @Qualifier("mainDataSource") DataSource ds) {
    return createEntityManagerFactory(ds, "sh4re_v2.sh4re_v2.domain.main");
  }

  @Bean
  public PlatformTransactionManager mainTransactionManager(
      @Qualifier("mainEntityManagerFactory") EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  private LocalContainerEntityManagerFactoryBean createEntityManagerFactory(DataSource ds, String pkg) {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(ds);
    emf.setPackagesToScan(pkg);
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    emf.setJpaPropertyMap(Map.of(
        "hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
    ));

    return emf;
  }
}

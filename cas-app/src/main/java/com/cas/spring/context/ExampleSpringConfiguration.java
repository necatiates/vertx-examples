package com.cas.spring.context;

import org.apache.ignite.cache.hibernate.HibernateRegionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Simple Java Spring configuration to be used for the Spring example application. This configuration is mainly
 * composed of a database configuration and initial population via the script "products.sql" of the database for
 * querying by our Spring service bean.
 *
 * The Spring service bean and repository are scanned for via @EnableJpaRepositories and @ComponentScan annotations
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.cas.spring.repository"})
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan("com.cas.spring.service")
public class ExampleSpringConfiguration {

  @Autowired
  private Environment env;

  @Bean
  @Autowired
  public DataSource dataSource(DatabasePopulator populator) {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
    dataSource.setUrl(env.getProperty("jdbc.url"));
    dataSource.setUsername(env.getProperty("jdbc.username"));
    dataSource.setPassword(env.getProperty("jdbc.password"));
    DatabasePopulatorUtils.execute(populator, dataSource);
    return dataSource;
  }

  @Bean(name="helloBean")
  @Autowired
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
    final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setPackagesToScan("com.cas.spring.entity");
    factoryBean.getJpaPropertyMap().put("hibernate.jdbc.batch_size", 0);
    factoryBean.getJpaPropertyMap().put("hibernate.use_sql_comments", true);
    factoryBean.getJpaPropertyMap().put("hibernate.show_sql", true);
    factoryBean.getJpaPropertyMap().put("org.apache.ignite.hibernate.grid_name", "meceap-grid");
    factoryBean.getJpaPropertyMap().put("hibernate.cache.region.factory_class", HibernateRegionFactory.class.getCanonicalName());
    factoryBean.getJpaPropertyMap().put("hibernate.cache.use_second_level_cache", true);
    factoryBean.getJpaPropertyMap().put("hibernate.cache.use_query_cache", true);
    factoryBean.getJpaPropertyMap().put("javax.persistence.sharedCache.mode", SharedCacheMode.ALL);
    factoryBean.getJpaPropertyMap().put("hibernate.cache.default_cache_concurrency_strategy", "read-write");
    factoryBean.getJpaPropertyMap().put("hibernate.generate_statistics", true);
    factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

    factoryBean.setMappingResources("com.cas.entity.User");


    return factoryBean;
  }

  @Bean
  @Autowired
  public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory.getObject());
  }

  @Bean
  @Autowired
  public DatabasePopulator databasePopulator() {
    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.setContinueOnError(false);
    return populator;
  }

}

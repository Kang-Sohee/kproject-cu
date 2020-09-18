package com.ohdocha.cu.kprojectcu.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableTransactionManagement
@ComponentScan({"org.springframework.boot.orm.jpa"})
@EnableJpaRepositories(
        entityManagerFactoryRef = "appEntityManagerFactory",
        transactionManagerRef = "appTransactionManager",
        basePackages = {"com.ohdocha.cu.kprojectcu.domain"}
)
public class DataSourceConfig {

    @Primary
    @Bean(name = "appDataSourceProperties")
    @ConfigurationProperties("cu.datasource")
    public CustomDataSourceProperties dataSourceProperties() {
        return new CustomDataSourceProperties();
    }

    @Primary
    @Bean(name = "appDataSource")
    @ConfigurationProperties("cu.datasource.configuration")
    public DataSource dataSource(@Qualifier("appDataSourceProperties") CustomDataSourceProperties appDataSourceProperties) {
        HikariDataSource hikariDataSource = appDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

//        hikariDataSource.setMaximumPoolSize(appDataSourceProperties.getMaximumPoolSize());
        return hikariDataSource;
    }

    @Primary
    @Bean(name = "appEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("appDataSource") DataSource appDataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.implicit_naming_strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl");
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        properties.put("hibernate.ddl-auto", "update");

        return builder
                .dataSource(appDataSource)
                .properties(properties)
                .packages("com.ohdocha.cu.kprojectcu.domain")
                .build();
    }

    @Primary
    @Bean(name = "appTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("appEntityManagerFactory") EntityManagerFactory appEntityManagerFactory) {
        return new JpaTransactionManager(appEntityManagerFactory);
    }

}

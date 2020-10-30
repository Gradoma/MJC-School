package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:db/database.properties")
public class SpringConfig {
    @Value("${db.driver}")
    private String dbDriver;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPassword;

    @Bean
    public JdbcTemplate getJdbcTemplate(BasicDataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public BasicDataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(dbDriver);
        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);
        return ds;
    }

    @Bean
    private static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

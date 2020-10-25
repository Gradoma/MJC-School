package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:db/database.properties")
public class DbConfig {
    @Value("${db.driver}")
    String dbDriver;
    @Value("${db.url}")
    String dbUrl;
    @Value("${db.user}")
    String dbUser;
    @Value("${db.password}")
    String dbPassword;



    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

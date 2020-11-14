package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:db/database.properties")
public class DbProperties {
    @Value("${db.driver}")
    private String dbDriver;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPassword;

    public String getDriver(){
        return dbDriver;
    }

    public String getUser(){
        return dbUser;
    }

    public String getPassword(){
        return dbPassword;
    }

    public String getUrl(){
        return dbUrl;
    }
}

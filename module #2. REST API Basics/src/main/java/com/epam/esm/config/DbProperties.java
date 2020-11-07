package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:db/database.properties")
public class DbProperties {
    @Value("${" + DRIVER + "}")
    private String dbDriver;
    @Value("${" + URL + "}")
    private String dbUrl;
    @Value("${" + USER + "}")
    private String dbUser;
    @Value("${" + PASSWORD + "}")
    private String dbPassword;

    private static final String DRIVER = "db.driver";
    private static final String USER = "db.user";
    private static final String PASSWORD = "db.password";
    private static final String URL = "db.url";

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

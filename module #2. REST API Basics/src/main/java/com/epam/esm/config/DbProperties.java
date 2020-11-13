package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

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
        // NOT the same as define in properties file - dbUser already equals "root". WHY??
        // define in database.properties as name in environment (DB_USER)
        return dbUser;
    }

    public String getPassword(){
        //dbPassword from properties file - need to call system.getEnv(name) to get value of environment variable
        return System.getenv(dbPassword);
    }

    public String getUrl(){
        return dbUrl;
    }
}

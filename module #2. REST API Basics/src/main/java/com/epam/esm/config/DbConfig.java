package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:db/database.properties")
public class DbConfig {
    @Autowired
    private final Environment environment;

    public DbConfig(Environment environment){
        this.environment = environment;
    }

    private static final String DRIVER = "db.driver";
    private static final String USER = "db.user";
    private static final String PASSWORD = "db.password";
    private static final String URL = "db.url";

    public String getDriver(){
        return environment.getProperty(DRIVER);
    }

    public String getUser(){
        return environment.getProperty(USER);
    }

    public String getPassword(){
        return environment.getProperty(PASSWORD);
    }

    public String getUrl(){
        return environment.getProperty(URL);
    }
}

package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.epam.esm")
public class TestApp {
    @Autowired
    private Environment env;
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

}

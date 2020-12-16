package com.epam.esm;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan("com.epam.esm")
public class TestConfig {
    @MockBean
    public SessionFactory sessionFactory;
}

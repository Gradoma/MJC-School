package com.epam.esm.config;

import com.epam.esm.dao.impl.TagDaoImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Import(TagDaoImpl.class)
public class SpringTestConfig {

    @Bean
    public JdbcTemplate getJdbcTemplate(@Qualifier("h2DataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("db/schema.sql")
                .build();
    }
}
